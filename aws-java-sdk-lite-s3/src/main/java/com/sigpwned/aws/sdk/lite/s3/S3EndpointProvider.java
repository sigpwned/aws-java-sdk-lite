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

import java.net.URI;
import com.sigpwned.aws.sdk.lite.core.Endpoint;
import com.sigpwned.aws.sdk.lite.core.aws.AwsEndpoint;
import com.sigpwned.aws.sdk.lite.core.util.AwsEndpoints;
import com.sigpwned.aws.sdk.lite.s3.util.S3;

@FunctionalInterface
public interface S3EndpointProvider {
  public static S3EndpointProvider defaultProvider() {
    return params -> Endpoint.builder()
        .url(URI.create(
            "https://" + AwsEndpoints.toHostname(AwsEndpoint.of(params.region(), S3.SERVICE_NAME))))
        .build();
  }

  public Endpoint resolveEndpoint(S3EndpointParams params);
}
