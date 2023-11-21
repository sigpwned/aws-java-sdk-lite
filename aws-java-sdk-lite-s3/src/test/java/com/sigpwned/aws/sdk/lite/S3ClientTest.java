package com.sigpwned.aws.sdk.lite;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsBasicCredentials;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsCredentials;
import com.sigpwned.aws.sdk.lite.s3.S3Client;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Request;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Response;

public abstract class S3ClientTest {
  @Rule
  public LocalStackContainer localstack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0.0"))
          .withServices(LocalStackContainer.Service.S3);

  public S3Client unit;

  @Before
  public void setupS3ClientTest() {
    final String endpoint = localstack.getEndpoint().toString();
    final AwsCredentials credentials =
        AwsBasicCredentials.of(localstack.getAccessKey(), localstack.getSecretKey());
    final String region = localstack.getRegion();
    unit = newClient(endpoint, credentials, region);
  }

  @After
  public void cleanupS3ClientTest() throws IOException {
    if (unit != null)
      unit.close();
    unit = null;
  }

  @Test
  public void smokeTest() {}

  @Test
  public void listObjects() throws IOException {
    ListObjectsV2Response response =
        unit.listObjectsV2(ListObjectsV2Request.builder().bucket("lasjhdckowjse").build());
    assertThat(response.contents(), is(not(empty())));
  }

  @Test
  public void getObject() throws IOException {
    ListObjectsV2Response response =
        unit.listObjectsV2(ListObjectsV2Request.builder().bucket("aleph0io").build());
    assertThat(response.contents(), is(not(empty())));
  }

  protected abstract S3Client newClient(String endpoint, AwsCredentials credentials, String region);
}
