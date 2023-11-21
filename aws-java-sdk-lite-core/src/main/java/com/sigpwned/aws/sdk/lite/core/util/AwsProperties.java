package com.sigpwned.aws.sdk.lite.core.util;

public final class AwsProperties {
  private AwsProperties() {}

  /**
   * Set on requests to track AWS service name
   */
  public static final String AWS_SERVICE_NAME = "aws.serviceName";

  /**
   * Set on requests to track AWS region
   */
  public static final String AWS_REGION = "aws.region";
}
