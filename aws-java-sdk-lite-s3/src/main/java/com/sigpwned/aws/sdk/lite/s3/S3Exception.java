package com.sigpwned.aws.sdk.lite.s3;

import com.sigpwned.aws.sdk.lite.core.AwsServiceException;

public abstract class S3Exception extends AwsServiceException {
  private static final long serialVersionUID = 2188205197124495354L;

  public S3Exception(String message) {
    super(message);
  }
}
