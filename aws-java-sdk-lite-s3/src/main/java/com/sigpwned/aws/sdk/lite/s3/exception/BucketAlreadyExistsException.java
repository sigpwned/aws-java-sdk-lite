package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.s3.S3Exception;

public class BucketAlreadyExistsException extends S3Exception {
  private static final long serialVersionUID = -7959949691445415982L;

  private final String bucket;

  public BucketAlreadyExistsException(String bucket) {
    super(bucket);
    if (bucket == null)
      throw new NullPointerException();
    this.bucket = bucket;
  }

  public String getBucket() {
    return bucket;
  }
}
