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
package com.sigpwned.aws.sdk.lite.core;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Map;
import com.sigpwned.aws.sdk.lite.core.http.SdkHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public abstract class SdkResponse {
  private ModelHttpResponseHead modelHttpResponse;

  public ModelHttpResponseHead modelHttpResponse() {
    return modelHttpResponse;
  }

  public SdkResponse modelHttpResponse(ModelHttpResponseHead modelHttpResponse) {
    this.modelHttpResponse = modelHttpResponse;
    return this;
  }

  public SdkHttpResponse sdkHttpResponse() {
    final ModelHttpResponseHead modelHttpResponse = modelHttpResponse();
    if (modelHttpResponse == null)
      throw new IllegalStateException("modelHttpResponse not set");
    return new SdkHttpResponse() {
      @Override
      public Map<String, List<String>> headers() {
        ModelHttpHeaders headers = modelHttpResponse.getHeaders();
        if (headers == null || headers.isEmpty())
          return emptyMap();
        return unmodifiableMap(headers.stream()
            .collect(groupingBy(Header::getName, mapping(Header::getValue, toList()))));
      }

      @Override
      public int statusCode() {
        return modelHttpResponse.getStatusCode();
      }
    };
  }
}
