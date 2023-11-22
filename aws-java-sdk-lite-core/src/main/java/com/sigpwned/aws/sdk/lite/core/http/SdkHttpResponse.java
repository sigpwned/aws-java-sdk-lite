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
package com.sigpwned.aws.sdk.lite.core.http;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public interface SdkHttpResponse extends SdkHttpHeaders {
  public static SdkHttpResponse fromModelHttpResponseHead(ModelHttpResponseHead response) {
    return new SdkHttpResponse() {
      @Override
      public Map<String, List<String>> headers() {
        return response.getHeaders().stream()
            .collect(groupingBy(Header::getName, mapping(Header::getValue, toList())));
      }

      @Override
      public int statusCode() {
        return response.getStatusCode();
      }
    };
  }

  default boolean isSuccessful() {
    return statusCode() / 100 == 2;
  }

  public int statusCode();

  default Optional<String> statusText() {
    return Optional.empty();
  }
}
