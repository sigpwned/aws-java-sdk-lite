/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-core
 * ====================================SECTION=====================================
 * Copyright (C) 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.aws.sdk.lite.core.auth.credentials.provider;

import static java.lang.String.format;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.auth.credentials.AwsSessionCredentials;
import com.sigpwned.aws.sdk.lite.core.util.ByteStreams;

public abstract class HttpAwsCredentialsProvider implements AwsCredentialsProvider {
  private AwsCredentials credentials;

  @Override
  public AwsCredentials resolveCredentials() {
    if (credentials == null)
      credentials = fetchCredentials();
    return credentials;
  }

  private AwsCredentials fetchCredentials() {
    // Resolve our endpoint
    final URI endpoint = resolveEndpoint();
    if (endpoint == null)
      return null;

    // Validate our endpoint
    try {
      if (!isAllowedHost(endpoint.getHost()))
        throw new IOException("Credentials endpoint must use loopback address");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    // Fetch our credentials
    JSONObject response;
    try {
      HttpURLConnection cn = connect(endpoint);
      try {
        if (cn.getResponseCode() == HttpURLConnection.HTTP_OK) {
          String responseBody =
              new String(ByteStreams.toByteArray(cn.getInputStream()), StandardCharsets.UTF_8);
          System.out.println("Received response: " + responseBody);
          response = new JSONObject(responseBody);
        } else {
          String errorBody =
              new String(ByteStreams.toByteArray(cn.getErrorStream()), StandardCharsets.UTF_8);
          throw new IOException(
              format("Request failed; status=%d; body=%s", cn.getResponseCode(), errorBody));
        }
      } finally {
        cn.disconnect();
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to retrieve container credentials", e);
    }

    String accessKeyId = response.getString("AccessKeyId");
    String secretAccessKey = response.getString("SecretAccessKey");
    String sessionToken = response.getString("Token");

    return AwsSessionCredentials.of(accessKeyId, secretAccessKey, sessionToken);
  }

  protected abstract URI resolveEndpoint();

  /**
   * hook
   */
  protected HttpURLConnection connect(URI endpoint) throws IOException {
    return (HttpURLConnection) endpoint.toURL().openConnection();
  }

  /**
   * Determines if the addresses for a given host are resolved to a loopback address.
   * <p>
   * This is a best-effort in determining what address a host will be resolved to. DNS caching might
   * be disabled, or could expire between this check and when the API is invoked.
   * </p>
   *
   * @param host The name or IP address of the host.
   * @return A boolean specifying whether the host is allowed as an endpoint for credentials
   *         loading.
   */
  private boolean isAllowedHost(String host) throws IOException {
    InetAddress[] addresses = InetAddress.getAllByName(host);
    boolean allAllowed = true;
    for (InetAddress address : addresses) {
      if (!address.isLoopbackAddress()) {
        allAllowed = false;
      }
    }
    return addresses.length > 0 && allAllowed;
  }
}
