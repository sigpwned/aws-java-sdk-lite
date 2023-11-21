package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.s3.S3Exception;

public class NoSuchBucketException extends S3Exception {
  private static final long serialVersionUID = 1740089369936939584L;

  private final String bucket;

  public NoSuchBucketException(String bucket) {
    super(bucket);
    if (bucket == null)
      throw new NullPointerException();
    this.bucket = bucket;
  }

  public String getBucket() {
    return bucket;
  }
}
