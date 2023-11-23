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
package com.sigpwned.aws.sdk.lite.s3.client;

import static java.util.Objects.requireNonNull;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import com.sigpwned.aws.sdk.lite.core.Endpoint;
import com.sigpwned.aws.sdk.lite.core.client.AwsClient;
import com.sigpwned.aws.sdk.lite.core.credentials.provider.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.credentials.provider.DefaultAwsCredentialsProviderChain;
import com.sigpwned.aws.sdk.lite.core.io.AbortableInputStream;
import com.sigpwned.aws.sdk.lite.core.io.RequestBody;
import com.sigpwned.aws.sdk.lite.core.io.ResponseInputStream;
import com.sigpwned.aws.sdk.lite.core.util.AwsRegions;
import com.sigpwned.aws.sdk.lite.s3.S3Client;
import com.sigpwned.aws.sdk.lite.s3.S3EndpointParams;
import com.sigpwned.aws.sdk.lite.s3.S3EndpointProvider;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.AccessDeniedExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.BucketAlreadyExistsExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.BucketAlreadyOwnedByYouExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.ExpiredTokenExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.NoSuchBucketExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.NoSuchKeyExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.exception.mapper.PermanentRedirectExceptionMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.CreateBucketRequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.CreateBucketResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.DeleteObjectRequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.DeleteObjectResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.DeleteObjectsRequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.DeleteObjectsResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.GetObjectRequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.GetObjectResponseAndObjectMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.HeadBucketRequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.HeadBucketResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.HeadObjectRequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.HeadObjectResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.ListObjectsV2RequestMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.ListObjectsV2ResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.PutObjectRequestAndObjectMapper;
import com.sigpwned.aws.sdk.lite.s3.mapper.PutObjectResponseMapper;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketRequest;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketResponse;
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
import com.sigpwned.aws.sdk.lite.s3.util.S3;
import com.sigpwned.httpmodel.core.client.bean.DefaultModelHttpBeanClient;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClient;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHeadBuilder;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class DefaultS3Client extends AwsClient implements S3Client {
  private final S3EndpointProvider endpointProvider;

  public DefaultS3Client() {
    this(DefaultAwsCredentialsProviderChain.getInstance());
  }

  public DefaultS3Client(AwsCredentialsProvider credentialsProvider) {
    this(new DefaultModelHttpBeanClient(), credentialsProvider,
        AwsRegions.findDefaultRegion().orElseThrow(), S3EndpointProvider.defaultProvider());
  }

  public DefaultS3Client(AwsCredentialsProvider credentialsProvider, String region,
      S3EndpointProvider endpointProvider) {
    this(new DefaultModelHttpBeanClient(), credentialsProvider, region, endpointProvider);
  }

  /* default */ DefaultS3Client(ModelHttpBeanClient client,
      AwsCredentialsProvider credentialsProvider, String region,
      S3EndpointProvider endpointProvider) {
    super(client, credentialsProvider, region, S3.SERVICE_NAME);
    this.endpointProvider = requireNonNull(endpointProvider);
    getClient().registerRequestMapper(new CreateBucketRequestMapper());
    getClient().registerResponseMapper(new CreateBucketResponseMapper());
    getClient().registerRequestMapper(new DeleteObjectRequestMapper());
    getClient().registerResponseMapper(new DeleteObjectResponseMapper());
    getClient().registerRequestMapper(new DeleteObjectsRequestMapper());
    getClient().registerResponseMapper(new DeleteObjectsResponseMapper());
    getClient().registerRequestMapper(new GetObjectRequestMapper());
    getClient().registerResponseMapper(new GetObjectResponseAndObjectMapper());
    getClient().registerRequestMapper(new HeadBucketRequestMapper());
    getClient().registerResponseMapper(new HeadBucketResponseMapper());
    getClient().registerRequestMapper(new HeadObjectRequestMapper());
    getClient().registerResponseMapper(new HeadObjectResponseMapper());
    getClient().registerRequestMapper(new ListObjectsV2RequestMapper());
    getClient().registerResponseMapper(new ListObjectsV2ResponseMapper());
    getClient().registerRequestMapper(new PutObjectRequestAndObjectMapper());
    getClient().registerResponseMapper(new PutObjectResponseMapper());
    getClient().registerExceptionMapper(new AccessDeniedExceptionMapper());
    getClient().registerExceptionMapper(new BucketAlreadyExistsExceptionMapper());
    getClient().registerExceptionMapper(new BucketAlreadyOwnedByYouExceptionMapper());
    getClient().registerExceptionMapper(new ExpiredTokenExceptionMapper());
    getClient().registerExceptionMapper(new NoSuchBucketExceptionMapper());
    getClient().registerExceptionMapper(new NoSuchKeyExceptionMapper());
    getClient().registerExceptionMapper(new PermanentRedirectExceptionMapper());
  }

  @Override
  public CreateBucketResponse createBucket(CreateBucketRequest request) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.PUT).build(), request,
          CreateBucketResponse.class);
    });
  }

  /**
   * https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadBucket.html
   */
  @Override
  public HeadBucketResponse headBucket(HeadBucketRequest request) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.HEAD).build(),
          request, HeadBucketResponse.class);
    });
  }

  /**
   * https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadObject.html
   */
  @Override
  public HeadObjectResponse headObject(HeadObjectRequest request) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.HEAD).build(),
          request, HeadObjectResponse.class);
    });
  }

  @Override
  public ResponseInputStream<GetObjectResponse> getObject(GetObjectRequest request) {
    return unsafe(() -> {
      // TODO Should we be parallel with RequestBody from putObject()? i.e., ResponseBody and
      // ContentStream?

      GetObjectResponseAndObject responseAndObject = getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.GET).build(), request,
          GetObjectResponseAndObject.class);

      ResponseInputStream<GetObjectResponse> result = null;
      final GetObjectResponse response = responseAndObject.getResponse();
      final ObjectByteSource object = responseAndObject.getObject();
      try {
        AbortableInputStream objectBytes = object.getBytes();
        try {
          result = new ResponseInputStream<>(response,
              AbortableInputStream.create(new FilterInputStream(objectBytes) {
                @Override
                public void close() {
                  try {
                    try {
                      super.close();
                    } finally {
                      object.close();
                    }
                  } catch (IOException e) {
                    throw new UncheckedIOException(e);
                  }
                }
              }, objectBytes));
        } finally {
          if (result == null)
            objectBytes.close();
        }
      } finally {
        if (result == null)
          object.close();
      }

      return result;
    });
  }

  @Override
  public DeleteObjectResponse deleteObject(DeleteObjectRequest request) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.DELETE).build(),
          request, DeleteObjectResponse.class);
    });
  }

  @Override
  public DeleteObjectsResponse deleteObjects(DeleteObjectsRequest request) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.POST).build(),
          request, DeleteObjectsResponse.class);
    });
  }

  @Override
  public PutObjectResponse putObject(PutObjectRequest request, RequestBody entity) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.PUT).build(),
          new PutObjectRequestAndObject(request, entity), PutObjectResponse.class);
    });
  }

  @Override
  public ListObjectsV2Response listObjectsV2(ListObjectsV2Request request) {
    return unsafe(() -> {
      return getClient().send(
          newHttpRequestHeadBuilder(request.bucket()).method(ModelHttpMethods.GET).build(), request,
          ListObjectsV2Response.class);
    });
  }

  @Override
  public ListObjectsV2Iterable listObjectsV2Paginator(ListObjectsV2Request request) {
    return new ListObjectsV2Iterable(this, request);
  }

  /**
   * hook
   */
  protected ModelHttpRequestHeadBuilder newHttpRequestHeadBuilder(String bucket) {
    Endpoint endpoint = endpointProvider
        .resolveEndpoint(S3EndpointParams.builder().bucket(bucket).region(getRegion()).build());

    ModelHttpRequestHeadBuilder result = ModelHttpRequestHead.builder();

    if (endpoint.headers() != null) {
      for (Map.Entry<String, List<String>> e : endpoint.headers().entrySet()) {
        String headerName = e.getKey();
        for (String headerValue : e.getValue()) {
          result.headers().addHeaderLast(headerName, headerValue).done();
        }
      }
    }

    if (endpoint.properties() != null) {
      for (Map.Entry<String, Object> e : endpoint.properties().entrySet()) {
        result.property(e.getKey(), e.getValue());
      }
    }

    return ModelHttpRequestHead.builder().url(ModelHttpUrl.fromUri(endpoint.url()));
  }
}
