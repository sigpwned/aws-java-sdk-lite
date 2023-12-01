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
package com.sigpwned.aws.sdk.lite.core.auth.credentials.provider.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import com.sigpwned.aws.sdk.lite.core.auth.credentials.provider.HttpAwsCredentialsProvider;
import com.sigpwned.httpmodel.core.util.ModelHttpHeaderNames;

public class ContainerAwsCredentialsProvider extends HttpAwsCredentialsProvider {
  /**
   * Environment variable to get the Amazon ECS credentials resource path.
   */
  private static final String AWS_CONTAINER_CREDENTIALS_RELATIVE_URI =
      "AWS_CONTAINER_CREDENTIALS_RELATIVE_URI";

  /**
   * Environment variable to get the full URI for a credentials path
   */
  private static final String AWS_CONTAINER_CREDENTIALS_FULL_URI =
      "AWS_CONTAINER_CREDENTIALS_FULL_URI";

  private static final String AWS_CONTAINER_AUTHORIZATION_TOKEN =
      "AWS_CONTAINER_AUTHORIZATION_TOKEN";

  /**
   * efault endpoint to retrieve the Amazon ECS Credentials.
   */
  private static final String ECS_CREDENTIALS_ENDPOINT = "http://169.254.170.2";

  @Override
  protected URI resolveEndpoint() {
    String containerCredentialsFullUri = System.getenv(AWS_CONTAINER_CREDENTIALS_FULL_URI);
    if (containerCredentialsFullUri != null)
      return URI.create(containerCredentialsFullUri);

    String containerCredentialsRelativeUri = System.getenv(AWS_CONTAINER_CREDENTIALS_RELATIVE_URI);
    if (containerCredentialsRelativeUri != null)
      return URI.create(ECS_CREDENTIALS_ENDPOINT + containerCredentialsRelativeUri);

    return null;
  }

  @Override
  protected HttpURLConnection connect(URI endpoint) throws IOException {
    HttpURLConnection result = super.connect(endpoint);

    String containerAuthorizationToken = System.getenv(AWS_CONTAINER_AUTHORIZATION_TOKEN);
    if (containerAuthorizationToken != null)
      result.setRequestProperty(ModelHttpHeaderNames.AUTHORIZATION, containerAuthorizationToken);

    return result;
  }
}
