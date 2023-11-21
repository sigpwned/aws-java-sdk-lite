package com.sigpwned.aws.sdk.lite.core.http;

import static java.util.Collections.unmodifiableList;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import com.sigpwned.aws.sdk.lite.core.thirdparty.com.amazonaws.util.SdkHttpUtils;

public interface SdkHttpRequest extends SdkHttpHeaders {
  /**
   * Returns the URL-encoded path that should be used in the HTTP request.
   */
  public String encodedPath();

  default Optional<String> encodedQueryParameters() {
    return encodeQueryParameters(rawQueryParameters(), SdkHttpRequest::encodeAsQueryParameter);
  }

  default Optional<String> encodedQueryParametersAsFormData() {
    return encodeQueryParameters(rawQueryParameters(), SdkHttpRequest::encodeAsFormParameter);
  }

  static Optional<String> encodeQueryParameters(Map<String, List<String>> rawQueryParameters,
      Function<String, String> encode) {
    if (rawQueryParameters.values().stream().mapToInt(List::size).sum() == 0)
      return Optional.empty();

    List<String> result = new ArrayList<>();
    for (Map.Entry<String, List<String>> e : rawQueryParameters.entrySet()) {
      String rawKey = e.getKey();
      String encodedKey = encode.apply(rawKey);
      for (String rawValue : e.getValue()) {
        String encodedValue = encode.apply(rawValue);
        result.add(encodedKey + "=" + encodedValue);
      }
    }

    return result.isEmpty() ? Optional.empty() : Optional.of(String.join("&", result));
  }

  static String encodeAsQueryParameter(String s) {
    return SdkHttpUtils.urlEncode(s, false);
  }

  static String encodeAsFormParameter(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("JDK does not support utf-8", e);
    }
  }

  default Optional<String> firstMatchingRawQueryParameter(String key) {
    List<String> matchingRawQueryParameters = firstMatchingRawQueryParameters(key);
    return Optional.ofNullable(
        matchingRawQueryParameters.isEmpty() ? null : matchingRawQueryParameters.get(0));

  }

  default Optional<String> firstMatchingRawQueryParameter(Collection<String> keys) {
    return keys.stream().map(key -> firstMatchingRawQueryParameter(key))
        .map(maybeValue -> maybeValue.orElse(null)).filter(Objects::nonNull).findFirst();
  }

  default List<String> firstMatchingRawQueryParameters(String key) {
    return Optional.ofNullable(rawQueryParameters().get(key.toLowerCase()))
        .orElseGet(Collections::emptyList);
  }

  default void forEachRawQueryParameter(BiConsumer<? super String, ? super List<String>> consumer) {
    for (Map.Entry<String, List<String>> e : rawQueryParameters().entrySet())
      consumer.accept(e.getKey(), unmodifiableList(e.getValue()));
  }

  public Map<String, List<String>> rawQueryParameters();
}
