package com.sigpwned.aws.sdk.lite.s3.mapper;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Optional;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectRequest;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class DeleteObjectRequestMapper
    implements ModelHttpBeanClientRequestMapper<DeleteObjectRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(DeleteObjectRequest.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      DeleteObjectRequest request) throws IOException {
    return httpRequestHead.toBuilder().method(ModelHttpMethods.DELETE).url().queryString()
        .setOnlyParameter("versionId",
            Optional.ofNullable(request.versionId()).map(Object::toString).orElse(null))
        .done().appendPath(format("%s/%s", request.bucket(), request.key())).done().headers()
        .setOnlyHeader("x-amz-bypass-governance-retention",
            Optional.ofNullable(request.bypassGovernanceRetention()).map(Object::toString)
                .orElse(null))
        .setOnlyHeader("x-amz-expected-bucket-owner",
            Optional.ofNullable(request.expectedBucketOwner()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-mfa",
            Optional.ofNullable(request.mfa()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-request-payer",
            Optional.ofNullable(request.requestPayer()).map(Object::toString).orElse(null))
        .done().build().toRequest();
  }
}
