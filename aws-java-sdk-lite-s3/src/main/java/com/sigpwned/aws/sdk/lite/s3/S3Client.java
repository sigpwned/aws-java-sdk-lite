package com.sigpwned.aws.sdk.lite.s3;

import com.sigpwned.aws.sdk.lite.core.io.RequestBody;
import com.sigpwned.aws.sdk.lite.core.io.ResponseInputStream;
import com.sigpwned.aws.sdk.lite.s3.client.DefaultS3ClientBuilder;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectResponse;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectsRequest;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectsResponse;
import com.sigpwned.aws.sdk.lite.s3.model.GetObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.GetObjectResponse;
import com.sigpwned.aws.sdk.lite.s3.model.HeadBucketRequest;
import com.sigpwned.aws.sdk.lite.s3.model.HeadBucketResponse;
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectResponse;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Iterable;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Request;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Response;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectResponse;

public interface S3Client extends AutoCloseable {
  public static S3ClientBuilder builder() {
    return new DefaultS3ClientBuilder();
  }

  public HeadBucketResponse headBucket(HeadBucketRequest request);

  public HeadObjectResponse headObject(HeadObjectRequest request);

  public ResponseInputStream<GetObjectResponse> getObject(GetObjectRequest request);

  public DeleteObjectResponse deleteObject(DeleteObjectRequest request);

  public DeleteObjectsResponse deleteObjects(DeleteObjectsRequest request);

  public PutObjectResponse putObject(PutObjectRequest request, RequestBody entity);

  public ListObjectsV2Response listObjectsV2(ListObjectsV2Request request);

  public ListObjectsV2Iterable listObjectsV2Paginator(ListObjectsV2Request request);

  @Override
  public void close();
}
