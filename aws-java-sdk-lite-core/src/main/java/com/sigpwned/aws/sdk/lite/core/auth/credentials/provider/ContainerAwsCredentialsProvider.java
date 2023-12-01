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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.auth.credentials.AwsSessionCredentials;
import com.sigpwned.aws.sdk.lite.core.util.ByteStreams;

/**
 * Fetches credentials from the endpoint in environment variable AWS_CONTAINER_CREDENTIALS_FULL_URI.
 * Assumes JSON response has structure {AccessKeyId, SecretKeyId, Token}, per existing code. Only
 * loads credentials once, and does not monitor token expiration. Appears to be used during
 * SnapStart only.
 */
public class ContainerAwsCredentialsProvider implements AwsCredentialsProvider {
  private AwsCredentials credentials;

  @Override
  public AwsCredentials resolveCredentials() {
    if (credentials == null)
      credentials = fetchCredentials();
    return credentials;
  }

  private static final String AWS_CONTAINER_CREDENTIALS_FULL_URI =
      "AWS_CONTAINER_CREDENTIALS_FULL_URI";

  private static final String AWS_CONTAINER_AUTHORIZATION_TOKEN =
      "AWS_CONTAINER_AUTHORIZATION_TOKEN";

  /**
   * https://github.com/aws/aws-sdk-java/blob/master/aws-java-sdk-core/src/main/java/com/amazonaws/auth/BaseCredentialsFetcher.java#L55C1-L62C1
   * https://github.com/aws/aws-sdk-java/blob/master/aws-java-sdk-core/src/main/java/com/amazonaws/internal/EC2ResourceFetcher.java#L38
   * https://github.com/aws/aws-sdk-java/blob/master/aws-java-sdk-core/src/main/java/com/amazonaws/internal/ConnectionUtils.java#L78
   * https://github.com/aws/aws-lambda-runtime-interface-emulator/blob/bf7e2486034742b84a1a25d28478e147b5e65f06/lambda/rapi/handler/credentials.go#L20
   * https://github.com/aws/aws-sdk-java-v2/blob/master/core/auth/src/main/java/software/amazon/awssdk/auth/credentials/ContainerCredentialsProvider.java
   * https://github.com/aws/aws-sdk-java-v2/blob/master/core/auth/src/main/java/software/amazon/awssdk/auth/credentials/internal/HttpCredentialsLoader.java
   */
  private AwsCredentials fetchCredentials() {
    String containerCredentialsFullUri = System.getenv(AWS_CONTAINER_CREDENTIALS_FULL_URI);
    String containerAuthorizationToken = System.getenv(AWS_CONTAINER_AUTHORIZATION_TOKEN);
    if (containerCredentialsFullUri == null || containerAuthorizationToken == null)
      return null;

    JSONObject response;

    try {
      HttpURLConnection cn =
          (HttpURLConnection) new URL(containerCredentialsFullUri).openConnection();
      try {
        cn.setRequestProperty("authorization", containerAuthorizationToken);
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
}
