package com.sigpwned.aws.sdk.lite.s3.mapper;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Request;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public class ListObjectsV2RequestMapper
    implements ModelHttpBeanClientRequestMapper<ListObjectsV2Request> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(ListObjectsV2Request.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      ListObjectsV2Request value) throws IOException {
    return httpRequestHead.toBuilder().url().queryString().setOnlyParameter("list-type", "2")
        .setOnlyParameter("continuation-token", value.continuationToken())
        .setOnlyParameter("delimiter", value.delimiter())
        .setOnlyParameter("encoding-type", value.encodingType())
        .setOnlyParameter("fetch-owner",
            Optional.ofNullable(value.fetchOwner()).map(Objects::toString).orElse(null))
        .setOnlyParameter("max-keys",
            Optional.ofNullable(value.maxKeys()).map(Objects::toString).orElse(null))
        .setOnlyParameter("prefix", value.prefix())
        .setOnlyParameter("start-after", value.startAfter()).done()
        .appendPath(format("%s/", value.bucket())).done().headers()
        .setOnlyHeader("x-amz-request-payer", value.requestPayer())
        .setOnlyHeader("x-amz-expected-bucket-owner", value.expectedBucketOwner())
        .setOnlyHeader("x-amz-optional-object-attributes",
            value.optionalObjectAttributes() != null && !value.optionalObjectAttributes().isEmpty()
                ? String.join(", ", value.optionalObjectAttributes())
                : null)
        .done().properties(httpRequestHead.getProperties()).build().toRequest();
  }
}
