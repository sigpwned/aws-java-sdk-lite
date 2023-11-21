package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchBucketException;
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class HeadBucketResponseMapper
    implements ModelHttpBeanClientResponseMapper<HeadObjectResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(HeadObjectResponse.class);
  }

  @Override
  public HeadObjectResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse response) throws IOException {
    try {
      String bucket = httpRequestHead.getHeaders()
          .findFirstHeaderByName(HeadBucketRequestMapper.X_S3_BUCKET_HEADER_NAME)
          .map(Header::getValue).get();

      // As documented
      // https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadBucket.html#API_HeadBucket_Errors
      // We also see 404 in the wild, which makes a lot more sense if we're honest
      if (response.getStatusCode() == ModelHttpStatusCodes.BAD_REQUEST
          || response.getStatusCode() == ModelHttpStatusCodes.NOT_FOUND)
        throw new NoSuchBucketException(bucket);

      return new HeadObjectResponse();
    } finally {
      response.close();
    }
  }
}
