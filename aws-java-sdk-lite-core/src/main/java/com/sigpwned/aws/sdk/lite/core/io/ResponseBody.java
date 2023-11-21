package com.sigpwned.aws.sdk.lite.core.io;

public class ResponseBody {
  private final Long contentLength;
  private final String contentType;
  private final ContentStreamProvider contentStreamProvider;

  public ResponseBody(Long contentLength, String contentType,
      ContentStreamProvider contentStreamProvider) {
    if (contentStreamProvider == null)
      throw new NullPointerException();
    this.contentLength = contentLength;
    this.contentType = contentType;
    this.contentStreamProvider = contentStreamProvider;
  }

  public Long getContentLength() {
    return contentLength;
  }

  public String getContentType() {
    return contentType;
  }

  public ContentStreamProvider getContentStreamProvider() {
    return contentStreamProvider;
  }
}
