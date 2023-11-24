/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-s3
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
package com.sigpwned.aws.sdk.lite.s3.client;

import com.sigpwned.aws.sdk.lite.core.credentials.provider.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.s3.S3ClientBuilder;
import com.sigpwned.aws.sdk.lite.s3.S3EndpointProvider;

public class DefaultS3ClientBuilder extends S3ClientBuilder {
  @Override
  public DefaultS3Client build() {
    return new DefaultS3Client(credentialsProvider(), region(), endpointProvider());
  }

  @Override
  public DefaultS3ClientBuilder endpointProvider(S3EndpointProvider endpointProvider) {
    return (DefaultS3ClientBuilder) super.endpointProvider(endpointProvider);
  }

  @Override
  public DefaultS3ClientBuilder credentialsProvider(AwsCredentialsProvider credentialsProvider) {
    return (DefaultS3ClientBuilder) super.credentialsProvider(credentialsProvider);
  }

  @Override
  public DefaultS3ClientBuilder region(String region) {
    return (DefaultS3ClientBuilder) super.region(region);
  }
}
