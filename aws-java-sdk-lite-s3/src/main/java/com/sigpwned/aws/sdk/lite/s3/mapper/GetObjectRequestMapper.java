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
package com.sigpwned.aws.sdk.lite.s3.mapper;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Optional;
import com.sigpwned.aws.sdk.lite.s3.model.GetObjectRequest;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class GetObjectRequestMapper implements ModelHttpBeanClientRequestMapper<GetObjectRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(GetObjectRequest.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead, GetObjectRequest request)
      throws IOException {
    return httpRequestHead.toBuilder().method(ModelHttpMethods.GET).url().queryString()
        .setOnlyParameter("partNumber",
            Optional.ofNullable(request.partNumber()).map(Object::toString).orElse(null))
        .setOnlyParameter("versionId",
            Optional.ofNullable(request.versionId()).map(Object::toString).orElse(null))
        .done().appendPath(format("%s/%s", request.bucket(), request.key())).done().headers()
        .setOnlyHeader("If-Match",
            Optional.ofNullable(request.ifMatch()).map(Object::toString).orElse(null))
        .setOnlyHeader("If-Modified-Since",
            Optional.ofNullable(request.ifModifiedSince()).map(Object::toString).orElse(null))
        .setOnlyHeader("If-None-Match",
            Optional.ofNullable(request.ifNoneMatch()).map(Object::toString).orElse(null))
        .setOnlyHeader("If-Unmodified-Since",
            Optional.ofNullable(request.ifUnmodifiedSince()).map(Object::toString).orElse(null))
        .setOnlyHeader("Range",
            Optional.ofNullable(request.range()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-server-side-encryption-customer-algorithm",
            Optional.ofNullable(request.sseCustomerAlgorithm()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-server-side-encryption-customer-key",
            Optional.ofNullable(request.sseCustomerKey()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-server-side-encryption-customer-key-MD5",
            Optional.ofNullable(request.sseCustomerKeyMD5()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-request-payer",
            Optional.ofNullable(request.requestPayer()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-expected-bucket-owner",
            Optional.ofNullable(request.expectedBucketOwner()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-checksum-mode",
            Optional.ofNullable(request.checksumMode()).map(Object::toString).orElse(null))
        .done().build().toRequest();

  }
}
