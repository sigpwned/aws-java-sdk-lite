package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.s3.S3Exception;

public class BucketAlreadyOwnedByYouException extends S3Exception {
  private static final long serialVersionUID = 2271607416218220119L;

  private final String bucket;

  public BucketAlreadyOwnedByYouException(String bucket) {
    super(bucket);
    if (bucket == null)
      throw new NullPointerException();
    this.bucket = bucket;
  }

  public String getBucket() {
    return bucket;
  }
}
