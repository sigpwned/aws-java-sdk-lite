package com.sigpwned.aws.sdk.lite.core.credentials;

import java.util.Objects;

public class AwsBasicCredentials implements AwsCredentials {
  public static AwsBasicCredentials of(String accessKeyId, String secretAccessKey) {
    return new AwsBasicCredentials(accessKeyId, secretAccessKey);
  }

  public static AwsBasicCredentials create(String accessKeyId, String secretAccessKey) {
    return of(accessKeyId, secretAccessKey);
  }

  private final String accessKeyId;
  private final String secretAccessKey;

  public AwsBasicCredentials(String accessKeyId, String secretAccessKey) {
    if (accessKeyId == null)
      throw new NullPointerException();
    accessKeyId = accessKeyId.trim();
    if (accessKeyId.isEmpty())
      throw new IllegalArgumentException("accessKeyId must not be empty");
    this.accessKeyId = accessKeyId;

    if (secretAccessKey == null)
      throw new NullPointerException();
    secretAccessKey = secretAccessKey.trim();
    if (secretAccessKey.isEmpty())
      throw new IllegalArgumentException("secretAccessKey must not be empty");
    this.secretAccessKey = secretAccessKey;
  }

  @Override
  public String accessKeyId() {
    return accessKeyId;
  }

  @Override
  public String secretAccessKey() {
    return secretAccessKey;
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessKeyId, secretAccessKey);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AwsBasicCredentials other = (AwsBasicCredentials) obj;
    return Objects.equals(accessKeyId, other.accessKeyId)
        && Objects.equals(secretAccessKey, other.secretAccessKey);
  }

  @Override
  public String toString() {
    return "AwsBasicCredentials";
  }
}
