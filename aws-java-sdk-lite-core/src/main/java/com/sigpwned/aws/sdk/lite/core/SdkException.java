package com.sigpwned.aws.sdk.lite.core;

public class SdkException extends RuntimeException {
  private static final long serialVersionUID = -7453158983259257492L;

  public SdkException(String message, Throwable cause) {
    super(message, cause);
  }

  public SdkException(String message) {
    super(message);
  }

  public SdkException(Throwable cause) {
    super(cause);
  }
}
