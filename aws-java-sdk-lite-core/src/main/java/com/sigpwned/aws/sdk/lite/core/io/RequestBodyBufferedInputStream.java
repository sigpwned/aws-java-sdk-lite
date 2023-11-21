package com.sigpwned.aws.sdk.lite.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class RequestBodyBufferedInputStream extends BufferedInputStream {
  private final RequestBody requestBody;

  public RequestBodyBufferedInputStream(RequestBody requestBody) {
    if (requestBody == null)
      throw new NullPointerException();
    this.requestBody = requestBody;
  }

  @Override
  public OptionalLong length() throws IOException {
    Long contentLength = getRequestBody().getContentLength();
    return contentLength != null ? OptionalLong.of(contentLength.longValue())
        : OptionalLong.empty();
  }

  @Override
  protected InputStream newInputStream() throws IOException {
    return getRequestBody().getContentStreamProvider().newStream();
  }

  private RequestBody getRequestBody() {
    return requestBody;
  }
}
