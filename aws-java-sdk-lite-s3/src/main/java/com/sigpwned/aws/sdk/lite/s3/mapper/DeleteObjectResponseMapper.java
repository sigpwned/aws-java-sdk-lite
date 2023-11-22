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
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public class DeleteObjectResponseMapper
    implements ModelHttpBeanClientResponseMapper<DeleteObjectResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(DeleteObjectResponse.class);
  }

  @Override
  public DeleteObjectResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    try {
      ModelHttpHeaders hs = httpResponse.getHeaders();
      return DeleteObjectResponse.builder()
          .versionId(hs.findFirstHeaderByName("x-amz-version-id").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .requestCharged(hs.findFirstHeaderByName("x-amz-request-charged").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .deleteMarker(hs.findFirstHeaderByName("x-amz-delete-marker").map(Header::getValue)
              .map(Boolean::valueOf).orElse(null))
          .build();
    } finally {
      httpResponse.close();
    }
  }
}
