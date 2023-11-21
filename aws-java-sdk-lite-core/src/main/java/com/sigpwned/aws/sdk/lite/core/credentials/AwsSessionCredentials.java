package com.sigpwned.aws.sdk.lite.core.credentials;

import java.util.Objects;

public class AwsSessionCredentials implements AwsCredentials {
  public static AwsSessionCredentials of(String accessKeyId, String secretAccessKey,
      String sessionToken) {
    return new AwsSessionCredentials(accessKeyId, secretAccessKey, sessionToken);
  }

  private final String accessKeyId;
  private final String secretAccessKey;
  private final String sessionToken;

  public AwsSessionCredentials(String accessKeyId, String secretAccessKey, String sessionToken) {
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

    if (sessionToken == null)
      throw new NullPointerException();
    sessionToken = sessionToken.trim();
    if (sessionToken.isEmpty())
      throw new IllegalArgumentException("sessionToken must not be empty");
    this.sessionToken = sessionToken;
  }

  @Override
  public String accessKeyId() {
    return accessKeyId;
  }

  @Override
  public String secretAccessKey() {
    return secretAccessKey;
  }

  public String sessionToken() {
    return sessionToken;
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessKeyId, secretAccessKey, sessionToken);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AwsSessionCredentials other = (AwsSessionCredentials) obj;
    return Objects.equals(accessKeyId, other.accessKeyId)
        && Objects.equals(secretAccessKey, other.secretAccessKey)
        && Objects.equals(sessionToken, other.sessionToken);
  }

  @Override
  public String toString() {
    return "AwsSessionCredentials";
  }
}
