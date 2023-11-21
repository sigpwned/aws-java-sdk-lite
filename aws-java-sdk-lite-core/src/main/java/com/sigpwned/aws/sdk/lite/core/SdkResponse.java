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
