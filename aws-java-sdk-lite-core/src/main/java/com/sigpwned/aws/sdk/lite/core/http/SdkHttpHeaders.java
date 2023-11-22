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

import static java.util.Collections.unmodifiableList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface SdkHttpHeaders {
  /**
   * Returns a map of all HTTP headers in this message, sorted in case-insensitive order by their
   * header name.
   */
  public Map<String, List<String>> headers();

  /**
   * Perform a case-insensitive search for a particular header in this request, returning the first
   * matching header, if one is found.
   */
  default Optional<String> firstMatchingHeader(String header) {
    List<String> matchingHeaders = matchingHeaders(header);
    return Optional.ofNullable(matchingHeaders.isEmpty() ? null : matchingHeaders.get(0));
  }

  default Optional<String> firstMatchingHeader(Collection<String> headersToFind) {
    return headersToFind.stream().map(header -> firstMatchingHeader(header))
        .map(maybeHeader -> maybeHeader.orElse(null)).filter(Objects::nonNull).findFirst();
  }

  default void forEachHeader(BiConsumer<? super String, ? super List<String>> consumer) {
    for (Map.Entry<String, List<String>> e : headers().entrySet()) {
      consumer.accept(e.getKey(), unmodifiableList(e.getValue()));
    }
  }

  default List<String> matchingHeaders(String header) {
    return Optional.ofNullable(headers().get(header.toLowerCase()))
        .map(Collections::unmodifiableList).orElseGet(Collections::emptyList);
  }

  default int numHeaders() {
    return headers().values().stream().mapToInt(List::size).sum();
  }
}
