package com.sigpwned.aws.sdk.lite.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class ResponseBodyBufferedInputStream extends BufferedInputStream {
  private final ResponseBody responseBody;

  public ResponseBodyBufferedInputStream(ResponseBody responseBody) {
    if (responseBody == null)
      throw new NullPointerException();
    this.responseBody = responseBody;
  }

  @Override
  public OptionalLong length() throws IOException {
    Long contentLength = getResponseBody().getContentLength();
    return contentLength != null ? OptionalLong.of(contentLength.longValue())
        : OptionalLong.empty();
  }

  @Override
  protected InputStream newInputStream() throws IOException {
    return getResponseBody().getContentStreamProvider().newStream();
  }

  private ResponseBody getResponseBody() {
    return responseBody;
  }
}
