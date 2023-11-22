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
package com.sigpwned.aws.sdk.lite.core.credentials;

import java.util.Objects;

public class AwsBasicCredentials implements AwsCredentials {
  public static AwsBasicCredentials of(String accessKeyId, String secretAccessKey) {
    return new AwsBasicCredentials(accessKeyId, secretAccessKey);
  }

  public static AwsBasicCredentials create(String accessKeyId, String secretAccessKey) {
    return of(accessKeyId, secretAccessKey);
  }

  private final String accessKeyId;
  private final String secretAccessKey;

  public AwsBasicCredentials(String accessKeyId, String secretAccessKey) {
    if (accessKeyId == null)
      throw new NullPointerException();
    accessKeyId = accessKeyId.trim();
    if (accessKeyId.isEmpty())
      throw new IllegalArgumentException("accessKeyId must not be empty");
    this.accessKeyId = accessKeyId;

    if (secretAccessKey == null)
      throw new NullPointerException();
    secretAccessKey = secretAccessKey.trim();
    if (secretAccessKey.isEmpty())
      throw new IllegalArgumentException("secretAccessKey must not be empty");
    this.secretAccessKey = secretAccessKey;
  }

  @Override
  public String accessKeyId() {
    return accessKeyId;
  }

  @Override
  public String secretAccessKey() {
    return secretAccessKey;
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessKeyId, secretAccessKey);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AwsBasicCredentials other = (AwsBasicCredentials) obj;
    return Objects.equals(accessKeyId, other.accessKeyId)
        && Objects.equals(secretAccessKey, other.secretAccessKey);
  }

  @Override
  public String toString() {
    return "AwsBasicCredentials";
  }
}
