package com.sigpwned.aws.sdk.lite.core;

import com.sigpwned.aws.sdk.lite.core.model.AwsErrorDetails;

public abstract class AwsServiceException extends SdkServiceException {
  private static final long serialVersionUID = -1224341353156716684L;

  private AwsErrorDetails awsErrorDetails;

  public AwsServiceException(String message) {
    super(message);
  }

  public AwsErrorDetails awsErrorDetails() {
    return awsErrorDetails;
  }

  public void awsErrorDetails(AwsErrorDetails awsErrorDetails) {
    this.awsErrorDetails = awsErrorDetails;
  }
}
