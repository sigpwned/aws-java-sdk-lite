package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.s3.model.HeadBucketRequest;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class HeadBucketRequestMapper
    implements ModelHttpBeanClientRequestMapper<HeadBucketRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(HeadBucketRequest.class);
  }

  /* default */ static final String X_S3_BUCKET_PROPERTY_NAME = "s3.bucket";

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      HeadBucketRequest request) throws IOException {
    // We squirrel the bucket name away in a header so we can find it in the response mapper
    return httpRequestHead.toBuilder().method(ModelHttpMethods.HEAD).url()
        .appendPath(request.bucket()).done().property(X_S3_BUCKET_PROPERTY_NAME, request.bucket())
        .headers().setOnlyHeader("x-amz-expected-bucket-owner", request.expectedBucketOwner())
        .done().build().toRequest();
  }
}
