package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class CreateBucketResponseMapper
    implements ModelHttpBeanClientResponseMapper<CreateBucketResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(CreateBucketResponse.class);
  }

  @Override
  public CreateBucketResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    try {
      // TODO Generic failure?
      if (httpResponse.getStatusCode() != ModelHttpStatusCodes.OK)
        throw new IOException("generic failure");
      ModelHttpHeaders hs = httpResponse.getHeaders();
      return CreateBucketResponse.builder()
          .location(hs.findFirstHeaderValueByName("location").map(String::valueOf).orElse(null))
          .build();
    } finally {
      httpResponse.close();
    }
  }
}
