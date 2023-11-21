package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.core.AwsEndpoint;
import com.sigpwned.aws.sdk.lite.s3.S3Exception;

public class PermanentRedirectS3Exception extends S3Exception {
  private static final long serialVersionUID = 933925769090356983L;

  private final AwsEndpoint endpoint;

  public PermanentRedirectS3Exception(AwsEndpoint endpoint) {
    super("permanent redirect");
    if (endpoint == null)
      throw new NullPointerException();
    this.endpoint = endpoint;
  }

  public AwsEndpoint getEndpoint() {
    return endpoint;
  }
}
