package com.sigpwned.aws.sdk.lite.core.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import com.sigpwned.aws.sdk.lite.core.Abortable;

public class ResponseInputStream<ResponseT> extends FilterInputStream implements Abortable {
  private final Abortable abortable;
  private ResponseT response;

  public ResponseInputStream(ResponseT response, InputStream in) {
    this(response, AbortableInputStream.create(in));
  }

  public ResponseInputStream(ResponseT response, AbortableInputStream in) {
    super(in);
    if (response == null)
      throw new NullPointerException();
    this.abortable = in;
    this.response = response;
  }

  public ResponseT getResponse() {
    return response;
  }

  public void setResponse(ResponseT response) {
    this.response = response;
  }

  private Abortable getAbortable() {
    return abortable;
  }

  @Override
  public void abort() {
    getAbortable().abort();
  }
}
