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
package com.sigpwned.aws.sdk.lite.core.util;

import java.util.Optional;
import com.sigpwned.aws.sdk.lite.core.AwsEndpoint;
import com.sigpwned.aws.sdk.lite.core.AwsServiceException;
import com.sigpwned.aws.sdk.lite.core.http.SdkHttpResponse;
import com.sigpwned.aws.sdk.lite.core.model.AwsErrorDetails;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public final class AwsServiceExceptions {
  private AwsServiceExceptions() {}

  public static <X extends AwsServiceException> X populate(X exception,
      ModelHttpRequestHead httpRequestHead, ModelHttpResponseHead httpResponseHead,
      ErrorMessage message) {
    SdkServiceExceptions.populate(exception, httpRequestHead, httpResponseHead, message);

    AwsEndpoint endpoint;
    try {
      endpoint =
          AwsEndpoints.fromHostname(httpRequestHead.getUrl().getAuthority().getHost().asHostname());
    } catch (Exception e) {
      // That's fine. It's just a custom hostname.
      endpoint = null;
    }

    exception.awsErrorDetails(
        AwsErrorDetails.builder().errorCode(message.code()).errorMessage(message.message())
            .serviceName(Optional.ofNullable(endpoint).map(AwsEndpoint::getService).orElse(null))
            .sdkHttpResponse(SdkHttpResponse.fromModelHttpResponseHead(httpResponseHead)).build());

    return exception;
  }
}
