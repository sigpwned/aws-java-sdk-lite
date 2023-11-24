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

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.s3.model.HeadBucketRequest;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class HeadBucketRequestMapper
    implements ModelHttpBeanClientRequestMapper<HeadBucketRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(HeadBucketRequest.class);
  }

  /* default */ static final String X_S3_BUCKET_PROPERTY_NAME = "s3.bucket";

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      HeadBucketRequest request) throws IOException {
    // We squirrel the bucket name away in a header so we can find it in the response mapper
    return httpRequestHead.toBuilder().method(ModelHttpMethods.HEAD).url()
        .appendPath(request.bucket()).done().property(X_S3_BUCKET_PROPERTY_NAME, request.bucket())
        .headers().setOnlyHeader("x-amz-expected-bucket-owner", request.expectedBucketOwner())
        .done().build().toRequest();
  }
}
