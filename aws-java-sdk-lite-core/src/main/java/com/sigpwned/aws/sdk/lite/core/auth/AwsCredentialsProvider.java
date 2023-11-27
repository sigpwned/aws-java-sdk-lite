/*-
 * =================================LICENSE_START==================================
 * httpmodel-aws
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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

@FunctionalInterface
public interface AwsCredentialsProvider {
  /**
   * Returns {@link AwsCredentials} that can be used to authorize an AWS request. Each
   * implementation of AWSCredentialsProvider can choose its own strategy for loading credentials.
   * For example, an implementation might load credentials from an existing key management system,
   * or load new credentials when credentials are rotated.
   *
   * <p>
   * If an error occurs during the loading of credentials or credentials could not be found, a
   * runtime exception will be raised.
   * </p>
   *
   * @return AwsCredentials which the caller can use to authorize an AWS request.
   */
  public AwsCredentials resolveCredentials();
}
