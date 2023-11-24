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
package com.sigpwned.aws.sdk.lite.core.client;

import com.sigpwned.aws.sdk.lite.core.credentials.provider.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.credentials.provider.DefaultAwsCredentialsProviderChain;
import com.sigpwned.aws.sdk.lite.core.util.AwsRegions;

public abstract class AwsClientBuilder {
  private AwsCredentialsProvider credentialsProvider =
      DefaultAwsCredentialsProviderChain.getInstance();
  private String region = AwsRegions.findDefaultRegion().orElse(null);

  public AwsCredentialsProvider credentialsProvider() {
    return credentialsProvider;
  }

  public AwsClientBuilder credentialsProvider(AwsCredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
    return this;
  }

  public String region() {
    return region;
  }

  public AwsClientBuilder region(String region) {
    this.region = region;
    return this;
  }
}
