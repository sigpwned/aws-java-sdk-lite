package com.sigpwned.aws.sdk.lite.s3;

import com.sigpwned.aws.sdk.lite.core.client.AwsClientBuilder;

public abstract class S3ClientBuilder<B extends S3ClientBuilder<B>> extends AwsClientBuilder<B> {
  public abstract S3Client build();
}
