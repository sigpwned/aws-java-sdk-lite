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
package com.sigpwned.aws.sdk.lite.s3;

import com.sigpwned.aws.sdk.lite.core.client.AwsClientBuilder;

public abstract class S3ClientBuilder<B extends S3ClientBuilder<B>> extends AwsClientBuilder<B> {
  private S3EndpointProvider endpointProvider = S3EndpointProvider.defaultProvider();

  public S3EndpointProvider endpointProvider() {
    return endpointProvider;
  }

  @SuppressWarnings("unchecked")
  public B endpointProvider(S3EndpointProvider endpointProvider) {
    this.endpointProvider = endpointProvider;
    return (B) this;
  }

  public abstract S3Client build();
}
