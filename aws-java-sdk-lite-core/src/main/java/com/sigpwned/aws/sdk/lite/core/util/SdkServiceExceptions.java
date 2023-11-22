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

import com.sigpwned.aws.sdk.lite.core.SdkServiceException;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public final class SdkServiceExceptions {
  private SdkServiceExceptions() {}

  public static <X extends SdkServiceException> X populate(X exception,
      ModelHttpRequestHead httpRequestHead, ModelHttpResponseHead httpResponseHead,
      ErrorMessage message) {
    exception.extendedRequestId(
        httpResponseHead.getHeaders().findFirstHeaderValueByName("x-amx-id-2").orElse(null));
    exception.requestId(message.requestId());
    exception.statusCode(httpResponseHead.getStatusCode());
    return exception;
  }
}
