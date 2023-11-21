package com.sigpwned.aws.sdk.lite.core;

public abstract class SdkServiceException extends SdkException {
  private static final long serialVersionUID = 1089432262818299913L;

  private int statusCode;
  private String requestId;
  private String extendedRequestId;

  public SdkServiceException(String message) {
    super(message);
  }

  public int statusCode() {
    return statusCode;
  }

  public void statusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String requestId() {
    return requestId;
  }

  public void requestId(String requestId) {
    this.requestId = requestId;
  }

  public String extendedRequestId() {
    return extendedRequestId;
  }

  public void extendedRequestId(String extendedRequestId) {
    this.extendedRequestId = extendedRequestId;
  }
}
