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
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Request;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public class ListObjectsV2RequestMapper
    implements ModelHttpBeanClientRequestMapper<ListObjectsV2Request> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(ListObjectsV2Request.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      ListObjectsV2Request value) throws IOException {
    return httpRequestHead.toBuilder().url().queryString().setOnlyParameter("list-type", "2")
        .setOnlyParameter("continuation-token", value.continuationToken())
        .setOnlyParameter("delimiter", value.delimiter())
        .setOnlyParameter("encoding-type", value.encodingType())
        .setOnlyParameter("fetch-owner",
            Optional.ofNullable(value.fetchOwner()).map(Objects::toString).orElse(null))
        .setOnlyParameter("max-keys",
            Optional.ofNullable(value.maxKeys()).map(Objects::toString).orElse(null))
        .setOnlyParameter("prefix", value.prefix())
        .setOnlyParameter("start-after", value.startAfter()).done()
        .appendPath(format("%s/", value.bucket())).done().headers()
        .setOnlyHeader("x-amz-request-payer", value.requestPayer())
        .setOnlyHeader("x-amz-expected-bucket-owner", value.expectedBucketOwner())
        .setOnlyHeader("x-amz-optional-object-attributes",
            value.optionalObjectAttributes() != null && !value.optionalObjectAttributes().isEmpty()
                ? String.join(", ", value.optionalObjectAttributes())
                : null)
        .done().build().toRequest();
  }
}
