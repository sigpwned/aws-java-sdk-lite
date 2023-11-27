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
package com.sigpwned.aws.sdk.lite.core.auth;

import com.sigpwned.aws.sdk.lite.core.auth.credentials.AwsBasicCredentials;

public interface AwsCredentials {
  public static AwsCredentials of(String accessKeyId, String secretAccessKey) {
    return AwsBasicCredentials.of(accessKeyId, secretAccessKey);
  }

  /**
   * Retrieve the AWS access key, used to identify the user interacting with services.
   */
  public String accessKeyId();

  /**
   * Retrieve the AWS secret access key, used to authenticate the user interacting with services.
   */
  public String secretAccessKey();
}
