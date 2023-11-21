package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.s3.S3Exception;

public class NoSuchKeyException extends S3Exception {
  private static final long serialVersionUID = -4008897756901075820L;

  private final String key;

  public NoSuchKeyException(String key) {
    super(key);
    if (key == null)
      throw new NullPointerException();
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
