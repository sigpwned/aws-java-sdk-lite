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
package com.sigpwned.aws.sdk.lite.s3.util;

import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.aws.sdk.lite.core.util.AwsServiceExceptions;
import com.sigpwned.aws.sdk.lite.s3.S3Exception;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public final class S3Exceptions {
  private S3Exceptions() {}

  public static <X extends S3Exception> X populate(X exception,
      ModelHttpRequestHead httpRequestHead, ModelHttpResponseHead httpResponseHead,
      ErrorMessage message) {
    AwsServiceExceptions.populate(exception, httpRequestHead, httpResponseHead, message);
    return exception;
  }
}
