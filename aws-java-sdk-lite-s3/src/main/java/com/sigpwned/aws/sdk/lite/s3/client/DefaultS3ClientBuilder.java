package com.sigpwned.aws.sdk.lite.s3.client;

import com.sigpwned.aws.sdk.lite.s3.S3ClientBuilder;

public class DefaultS3ClientBuilder extends S3ClientBuilder<DefaultS3ClientBuilder> {
  @Override
  public DefaultS3Client build() {
    return new DefaultS3Client(credentialsProvider(), region());
  }
}
