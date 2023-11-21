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
