package com.sigpwned.aws.sdk.lite.client;

import java.net.URI;
import com.sigpwned.aws.sdk.lite.S3ClientTest;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsCredentials;
import com.sigpwned.aws.sdk.lite.s3.S3Client;
import com.sigpwned.aws.sdk.lite.s3.client.DefaultS3Client;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;

public class DefaultS3ClientTest extends S3ClientTest {
  @Override
  protected S3Client newClient(String endpoint, AwsCredentials credentials, String region) {
    return new DefaultS3Client(() -> credentials, region) {
      @Override
      protected ModelHttpUrl baseUrl() {
        return ModelHttpUrl.fromUri(URI.create(endpoint));
      }
    };
  }
}
