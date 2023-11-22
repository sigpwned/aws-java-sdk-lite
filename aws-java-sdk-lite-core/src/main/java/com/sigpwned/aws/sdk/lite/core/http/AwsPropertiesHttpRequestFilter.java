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
package com.sigpwned.aws.sdk.lite.core.http;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import com.sigpwned.aws.sdk.lite.core.util.AwsProperties;
import com.sigpwned.httpmodel.core.ModelHttpRequestFilter;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public class AwsPropertiesHttpRequestFilter implements ModelHttpRequestFilter {
  private final String serviceName;
  private final String region;

  public AwsPropertiesHttpRequestFilter(String serviceName, String region) {
    this.serviceName = requireNonNull(serviceName);
    this.region = requireNonNull(region);
  }

  @Override
  public void filter(ModelHttpRequestHead requestHead) throws IOException {
    requestHead.setProperty(AwsProperties.AWS_SERVICE_NAME, getServiceName());
    requestHead.setProperty(AwsProperties.AWS_REGION, getRegion());
  }

  private String getServiceName() {
    return serviceName;
  }

  private String getRegion() {
    return region;
  }
}
