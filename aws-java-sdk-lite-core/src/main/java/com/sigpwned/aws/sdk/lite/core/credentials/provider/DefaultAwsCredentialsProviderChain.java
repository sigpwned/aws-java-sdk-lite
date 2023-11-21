package com.sigpwned.aws.sdk.lite.core.credentials.provider;

import java.util.Arrays;

/**
 * https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html
 */
public class DefaultAwsCredentialsProviderChain extends AwsCredentialsProviderChain {
  public static final DefaultAwsCredentialsProviderChain getInstance() {
    return new DefaultAwsCredentialsProviderChain();
  }

  public DefaultAwsCredentialsProviderChain() {
    super(Arrays.asList(new EnvironmentVariableCredentialsProvider(),
        new SystemPropertiesCredentialsProvider()));
  }
}
