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
package com.sigpwned.aws.sdk.lite.core.aws;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.sigpwned.aws.sdk.lite.core.util.AwsEndpoints;
import com.sigpwned.aws.sdk.lite.core.util.AwsProperties;
import com.sigpwned.httpmodel.core.model.ModelHttpHost;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.host.ModelHttpHostnameHost;

public interface AwsSigner {
  default ModelHttpRequest sign(ModelHttpRequest request) {
    String serviceName, region;

    ModelHttpHost host = request.getUrl().getAuthority().getHost();
    if (host instanceof ModelHttpHostnameHost) {
      AwsEndpoint endpoint = AwsEndpoints
          .fromHostname(request.getUrl().getAuthority().getHost().asHostname().getHostname());
      serviceName = endpoint.getService();
      region = endpoint.getRegion();
    } else {
      serviceName = (String) request.getProperty(AwsProperties.AWS_SERVICE_NAME)
          .orElseThrow(() -> new IllegalArgumentException(
              "Request property " + AwsProperties.AWS_SERVICE_NAME + " not set"));
      region = (String) request.getProperty(AwsProperties.AWS_REGION)
          .orElseThrow(() -> new IllegalArgumentException(
              "Request property " + AwsProperties.AWS_REGION + " not set"));
    }

    return sign(request, region, serviceName);
  }

  default ModelHttpRequest sign(ModelHttpRequest request, String region, String service) {
    return sign(request, region, service, OffsetDateTime.now(ZoneOffset.UTC));
  }

  public ModelHttpRequest sign(ModelHttpRequest request, String region, String service,
      OffsetDateTime now);
}
