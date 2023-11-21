package com.sigpwned.aws.sdk.lite.core.credentials.provider;

import java.util.Objects;
import java.util.stream.Stream;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsSessionCredentials;
import com.sigpwned.aws.sdk.lite.core.util.MoreStrings;

/**
 * https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/SystemPropertiesCredentialsProvider.html
 */
public class SystemPropertiesCredentialsProvider implements AwsCredentialsProvider {
  public static final String AWS_ACCESS_KEY_ID_NAME = "aws.accessKeyId";

  public static final String AWS_SECRET_KEY_NAME = "aws.secretKey";

  public static final String AWS_SESSION_TOKEN_NAME = "aws.sessionToken";

  @Override
  public AwsCredentials resolveCredentials() {
    String accessKeyId = Stream.of(AWS_ACCESS_KEY_ID_NAME).map(System::getProperty)
        .map(MoreStrings::notNullOrBlank).filter(Objects::nonNull).findFirst().orElse(null);

    String secretAccessKey = Stream.of(AWS_SECRET_KEY_NAME).map(System::getProperty)
        .map(MoreStrings::notNullOrBlank).filter(Objects::nonNull).findFirst().orElse(null);

    String sessionToken = Stream.of(AWS_SESSION_TOKEN_NAME).map(System::getenv)
        .map(MoreStrings::notNullOrBlank).filter(Objects::nonNull).findFirst().orElse(null);

    if (accessKeyId != null && secretAccessKey != null && sessionToken != null)
      return AwsSessionCredentials.of(accessKeyId, secretAccessKey, sessionToken);

    if (accessKeyId != null && secretAccessKey != null)
      return AwsCredentials.of(accessKeyId, secretAccessKey);

    // TODO What should we throw?
    throw new IllegalStateException("no credentials");
  }
}
