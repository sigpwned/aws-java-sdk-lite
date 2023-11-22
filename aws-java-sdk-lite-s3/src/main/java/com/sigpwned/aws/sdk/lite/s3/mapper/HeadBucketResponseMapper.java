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
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchBucketException;
import com.sigpwned.aws.sdk.lite.s3.model.HeadBucketResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class HeadBucketResponseMapper
    implements ModelHttpBeanClientResponseMapper<HeadBucketResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(HeadBucketResponse.class);
  }

  @Override
  public HeadBucketResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse response) throws IOException {
    try {
      String bucket = httpRequestHead.getProperty(HeadBucketRequestMapper.X_S3_BUCKET_PROPERTY_NAME)
          .map(String.class::cast).get();

      // As documented
      // https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadBucket.html#API_HeadBucket_Errors
      // We also see 404 in the wild, which makes a lot more sense if we're honest
      if (response.getStatusCode() == ModelHttpStatusCodes.BAD_REQUEST
          || response.getStatusCode() == ModelHttpStatusCodes.NOT_FOUND)
        throw new NoSuchBucketException(bucket);

      return new HeadBucketResponse();
    } finally {
      response.close();
    }
  }
}
