package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public class DeleteObjectResponseMapper
    implements ModelHttpBeanClientResponseMapper<DeleteObjectResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(DeleteObjectResponse.class);
  }

  @Override
  public DeleteObjectResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    try {
      ModelHttpHeaders hs = httpResponse.getHeaders();
      return DeleteObjectResponse.builder()
          .versionId(hs.findFirstHeaderByName("x-amz-version-id").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .requestCharged(hs.findFirstHeaderByName("x-amz-request-charged").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .deleteMarker(hs.findFirstHeaderByName("x-amz-delete-marker").map(Header::getValue)
              .map(Boolean::valueOf).orElse(null))
          .build();
    } finally {
      httpResponse.close();
    }
  }
}
