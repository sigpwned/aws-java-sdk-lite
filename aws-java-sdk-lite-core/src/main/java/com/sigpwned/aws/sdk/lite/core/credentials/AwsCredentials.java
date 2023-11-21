package com.sigpwned.aws.sdk.lite.core.credentials;

public interface AwsCredentials {
  public static AwsCredentials of(String accessKeyId, String secretAccessKey) {
    return AwsBasicCredentials.of(accessKeyId, secretAccessKey);
  }

  /**
   * Retrieve the AWS access key, used to identify the user interacting with services.
   */
  public String accessKeyId();

  /**
   * Retrieve the AWS secret access key, used to authenticate the user interacting with services.
   */
  public String secretAccessKey();
}
