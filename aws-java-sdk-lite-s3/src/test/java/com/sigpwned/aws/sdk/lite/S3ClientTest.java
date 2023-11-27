/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-s3
 * ====================================SECTION=====================================
 * Copyright (C) 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.aws.sdk.lite;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.auth.credentials.AwsBasicCredentials;
import com.sigpwned.aws.sdk.lite.core.io.RequestBody;
import com.sigpwned.aws.sdk.lite.s3.S3Client;
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchBucketException;
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchKeyException;
import com.sigpwned.aws.sdk.lite.s3.model.CommonPrefix;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketRequest;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketResponse;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.GetObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.HeadBucketRequest;
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectResponse;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Request;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Response;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.S3Object;
import com.sigpwned.aws.sdk.lite.s3.util.StorageClasses;
import com.sigpwned.httpmodel.core.util.MoreByteStreams;

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
  public void createBucketTest() throws IOException {
    final String bucketName = "example-bucket-name";
    CreateBucketResponse response =
        unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    assertThat(response.location(), is("/" + bucketName));
  }

  @Test
  public void headBucketExistsTest() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    unit.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
  }

  @Test(expected = NoSuchBucketException.class)
  public void headBucketNotExistsTest() throws IOException {
    final String bucketName = "example-bucket-name";

    unit.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
  }

  @Test
  public void listObjectsEmpty() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    ListObjectsV2Response response =
        unit.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix("").build());
    assertThat(response.contents(), is(empty()));
  }

  @Test
  public void listObjectsOnPrefixWithDelimiter() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String object1 = prefix + delimiter + "bravo.txt";
    final byte[] object1Bytes = object1.getBytes(StandardCharsets.UTF_8);
    final String object2 = prefix + delimiter + "charlie" + delimiter + "delta.txt";
    final byte[] object2Bytes = object2.getBytes(StandardCharsets.UTF_8);

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object1).build(),
        RequestBody.fromBytes(object1Bytes));

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object2).build(),
        RequestBody.fromBytes(object2Bytes));

    ListObjectsV2Response response = unit.listObjectsV2(ListObjectsV2Request.builder()
        .bucket(bucketName).prefix(prefix).delimiter(delimiter).build());
    assertThat(response.contents(), is(empty()));
    assertThat(response.commonPrefixes(),
        is(asList(CommonPrefix.builder().prefix("alpha/").build())));
    assertThat(response.keyCount(), is(1));
  }

  @Test
  public void listObjectsOnPrefixWithoutDelimiter() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String object1 = prefix + delimiter + "bravo.txt";
    final byte[] object1Bytes = object1.getBytes(StandardCharsets.UTF_8);
    final String object2 = prefix + delimiter + "charlie" + delimiter + "delta.txt";
    final byte[] object2Bytes = object2.getBytes(StandardCharsets.UTF_8);

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object1).build(),
        RequestBody.fromBytes(object1Bytes));

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object2).build(),
        RequestBody.fromBytes(object2Bytes));

    ListObjectsV2Response response = unit
        .listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix).build());

    S3Object contents0 = response.contents().get(0);
    S3Object contents1 = response.contents().get(1);

    assertThat(response.contents(),
        is(asList(
            S3Object.builder().eTag("\"1b3844a45b5945cc34538cba5b1c6359\"").key(object1)
                .lastModified(contents0.lastModified()).size(Long.valueOf(object1Bytes.length))
                .storageClass(StorageClasses.STANDARD).build(),
            S3Object.builder().eTag("\"0d5ac3b68fa11bf83b7d1091c3fccc68\"").key(object2)
                .lastModified(contents1.lastModified()).size(Long.valueOf(object2Bytes.length))
                .storageClass(StorageClasses.STANDARD).build())));
    assertThat(response.commonPrefixes(), is(emptyList()));
    assertThat(response.keyCount(), is(2));
  }

  @Test
  public void listObjectsOffPrefixWithDelimiter() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix1 = "alpha";
    final String object1 = prefix1 + delimiter + "bravo.txt";
    final byte[] object1Bytes = object1.getBytes(StandardCharsets.UTF_8);
    final String object2 = prefix1 + delimiter + "charlie" + delimiter + "delta.txt";
    final byte[] object2Bytes = object2.getBytes(StandardCharsets.UTF_8);
    final String prefix2 = "foobar";

    assertThat(prefix2, is(not(prefix1)));

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object1).build(),
        RequestBody.fromBytes(object1Bytes));

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object2).build(),
        RequestBody.fromBytes(object2Bytes));

    ListObjectsV2Response response = unit.listObjectsV2(ListObjectsV2Request.builder()
        .bucket(bucketName).prefix(prefix2).delimiter(delimiter).build());
    assertThat(response.contents(), is(empty()));
    assertThat(response.commonPrefixes(), is(emptyList()));
    assertThat(response.keyCount(), is(0));
  }

  @Test
  public void listObjectsOffPrefixWithoutDelimiter() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix1 = "alpha";
    final String object1 = prefix1 + delimiter + "bravo.txt";
    final byte[] object1Bytes = object1.getBytes(StandardCharsets.UTF_8);
    final String object2 = prefix1 + delimiter + "charlie" + delimiter + "delta.txt";
    final byte[] object2Bytes = object2.getBytes(StandardCharsets.UTF_8);
    final String prefix2 = "foobar";

    assertThat(prefix2, is(not(prefix1)));

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object1).build(),
        RequestBody.fromBytes(object1Bytes));

    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(object2).build(),
        RequestBody.fromBytes(object2Bytes));

    ListObjectsV2Response response = unit
        .listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix2).build());

    assertThat(response.contents(), is(emptyList()));
    assertThat(response.commonPrefixes(), is(emptyList()));
    assertThat(response.keyCount(), is(0));
  }

  @Test
  public void getObjectExists() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String key = prefix + delimiter + "bravo.txt";
    final byte[] putContents = key.getBytes(StandardCharsets.UTF_8);
    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
        RequestBody.fromBytes(putContents));

    final byte[] getContents;
    try (InputStream in =
        unit.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build())) {
      getContents = MoreByteStreams.toByteArray(in);
    }

    assertThat(getContents, is(putContents));
  }

  @Test(expected = NoSuchKeyException.class)
  public void getObjectNotExists() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String key = prefix + delimiter + "bravo.txt";

    try (InputStream in =
        unit.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build())) {
    }
  }

  @Test
  public void headObjectExists() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String key = prefix + delimiter + "bravo.txt";
    final byte[] putContents = key.getBytes(StandardCharsets.UTF_8);
    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
        RequestBody.fromBytes(putContents));

    HeadObjectResponse response =
        unit.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());

    assertThat(response.contentLength(), is(15L));
  }

  @Test(expected = NoSuchKeyException.class)
  public void headObjectNotExists() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String key = prefix + delimiter + "bravo.txt";

    unit.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
  }

  @Test
  public void deleteObjectExists() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String key = prefix + delimiter + "bravo.txt";
    final byte[] putContents = key.getBytes(StandardCharsets.UTF_8);
    unit.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
        RequestBody.fromBytes(putContents));

    boolean existsBeforeDelete;
    try {
      unit.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
      existsBeforeDelete = true;
    } catch (NoSuchKeyException e) {
      existsBeforeDelete = false;
    }

    unit.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());

    boolean existsAfterDelete;
    try {
      unit.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
      existsAfterDelete = true;
    } catch (NoSuchKeyException e) {
      existsAfterDelete = false;
    }

    assertThat(existsBeforeDelete, is(true));
    assertThat(existsAfterDelete, is(false));
  }


  @Test
  public void deleteObjectNotExists() throws IOException {
    final String bucketName = "example-bucket-name";
    unit.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    final String delimiter = "/";
    final String prefix = "alpha";
    final String key = prefix + delimiter + "bravo.txt";

    unit.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());

    boolean existsAfterDelete;
    try {
      unit.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
      existsAfterDelete = true;
    } catch (NoSuchKeyException e) {
      existsAfterDelete = false;
    }

    assertThat(existsAfterDelete, is(false));
  }

  protected abstract S3Client newClient(String endpoint, AwsCredentials credentials, String region);
}
