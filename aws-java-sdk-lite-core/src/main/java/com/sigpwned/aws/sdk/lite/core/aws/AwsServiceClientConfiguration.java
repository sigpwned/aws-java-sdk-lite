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
package com.sigpwned.aws.sdk.lite.core.aws;

import java.net.URI;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.sdk.SdkServiceClientConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(fluent = true, chain = true)
public class AwsServiceClientConfiguration extends SdkServiceClientConfiguration {
  private AwsCredentialsProvider credentialsProvider;
  private String region;

  @Builder(builderMethodName = "awsBuilder")
  public AwsServiceClientConfiguration(URI endpointOverride,
      AwsCredentialsProvider credentialsProvider, String region) {
    super(endpointOverride);
    this.credentialsProvider = credentialsProvider;
    this.region = region;
  }
}
