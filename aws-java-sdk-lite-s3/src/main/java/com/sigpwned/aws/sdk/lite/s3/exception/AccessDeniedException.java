package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.s3.S3Exception;

public class AccessDeniedException extends S3Exception {
  private static final long serialVersionUID = -912799247367757936L;

  public AccessDeniedException() {
    super("access denied");
  }
}
