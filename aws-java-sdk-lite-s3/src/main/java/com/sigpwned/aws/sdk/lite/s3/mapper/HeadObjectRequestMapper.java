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
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectRequest;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class HeadObjectRequestMapper
    implements ModelHttpBeanClientRequestMapper<HeadObjectRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(HeadObjectRequest.class);
  }

  /* default */ static final String X_S3_BUCKET_PROPERTY_NAME = "s3.bucket";

  /* default */ static final String X_S3_KEY_PROPERTY_NAME = "s3.key";


  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      HeadObjectRequest request) throws IOException {
    // We squirrel the key away in a header so we can find it in the response mapper
    return httpRequestHead.toBuilder().method(ModelHttpMethods.HEAD).url().queryString()
        .setOnlyParameter("partNumber",
            Optional.ofNullable(request.partNumber()).map(Object::toString).orElse(null))
        .setOnlyParameter("versionId",
            Optional.ofNullable(request.versionId()).map(Object::toString).orElse(null))
        .done().appendPath(format("%s/%s", request.bucket(), request.key())).done()
        .property(X_S3_BUCKET_PROPERTY_NAME, request.bucket())
        .property(X_S3_KEY_PROPERTY_NAME, request.key()).headers()
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
