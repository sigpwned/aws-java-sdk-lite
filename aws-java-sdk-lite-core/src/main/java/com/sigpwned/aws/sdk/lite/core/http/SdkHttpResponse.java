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
