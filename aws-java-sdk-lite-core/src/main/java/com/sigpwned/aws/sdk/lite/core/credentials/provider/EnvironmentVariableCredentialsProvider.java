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
package com.sigpwned.aws.sdk.lite.core.credentials.provider;

import java.util.Objects;
import java.util.stream.Stream;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsSessionCredentials;
import com.sigpwned.aws.sdk.lite.core.util.MoreStrings;

/**
 * https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/EnvironmentVariableCredentialsProvider.html
 */
public class EnvironmentVariableCredentialsProvider implements AwsCredentialsProvider {
  /**
   * https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html#envvars-list-AWS_ACCESS_KEY_ID
   */
  public static final String AWS_ACCESS_KEY_ID_NAME = "AWS_ACCESS_KEY_ID";

  public static final String AWS_ACCESS_KEY_NAME = "AWS_ACCESS_KEY";

  /**
   * https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html#envvars-list-AWS_SECRET_ACCESS_KEY
   */
  public static final String AWS_SECRET_ACCESS_KEY_NAME = "AWS_SECRET_ACCESS_KEY";

  public static final String AWS_SECRET_KEY_NAME = "AWS_SECRET_KEY";

  /**
   * https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html#envvars-list-AWS_SESSION_TOKEN
   */
  public static final String AWS_SESSION_TOKEN_NAME = "AWS_SESSION_TOKEN";

  @Override
  public AwsCredentials resolveCredentials() {
    String accessKeyId = Stream.of(AWS_ACCESS_KEY_ID_NAME, AWS_ACCESS_KEY_NAME).map(System::getenv)
        .map(MoreStrings::notNullOrBlank).filter(Objects::nonNull).findFirst().orElse(null);

    String secretAccessKey =
        Stream.of(AWS_SECRET_ACCESS_KEY_NAME, AWS_SECRET_KEY_NAME).map(System::getenv)
            .map(MoreStrings::notNullOrBlank).filter(Objects::nonNull).findFirst().orElse(null);

    String sessionToken = Stream.of(AWS_SESSION_TOKEN_NAME).map(System::getenv)
        .map(MoreStrings::notNullOrBlank).filter(Objects::nonNull).findFirst().orElse(null);

    if (accessKeyId != null && secretAccessKey != null && sessionToken != null)
      return AwsSessionCredentials.of(accessKeyId, secretAccessKey, sessionToken);

    if (accessKeyId != null && secretAccessKey != null)
      return AwsCredentials.of(accessKeyId, secretAccessKey);

    // TODO What should we throw?
    throw new IllegalStateException("no credentials");
  }
}
