package com.sigpwned.aws.sdk.lite.core;

public class SdkClientException extends SdkException {
  private static final long serialVersionUID = -7597201441039758362L;

  public SdkClientException(String message) {
    super(message);
  }

  public SdkClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public SdkClientException(Throwable cause) {
    super(cause);
  }
}
