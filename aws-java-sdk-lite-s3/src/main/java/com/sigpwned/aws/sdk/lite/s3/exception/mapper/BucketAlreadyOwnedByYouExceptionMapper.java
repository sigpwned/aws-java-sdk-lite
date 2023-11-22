package com.sigpwned.aws.sdk.lite.s3.exception.mapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.aws.sdk.lite.s3.exception.BucketAlreadyOwnedByYouException;
import com.sigpwned.aws.sdk.lite.s3.util.ErrorMessages;
import com.sigpwned.aws.sdk.lite.s3.util.S3Exceptions;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientExceptionMapper;
import com.sigpwned.httpmodel.core.io.InputStreamBufferingStrategy;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

/**
 * <p>
 * Requested bucket already exists and is owned by you
 * </p>
 */
public class BucketAlreadyOwnedByYouExceptionMapper implements ModelHttpBeanClientExceptionMapper {

  @Override
  public BucketAlreadyOwnedByYouException mapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    if (httpResponse.getStatusCode() == ModelHttpStatusCodes.BAD_REQUEST) {
      httpResponse.buffer(InputStreamBufferingStrategy.MEMORY);
      ErrorMessage error = ErrorMessages.fromString(httpResponse.toString(StandardCharsets.UTF_8));
      httpResponse.restart();
      if (error.code().equals(ErrorMessages.CODE_BUCKET_ALREADY_OWNED_BY_YOU))
        return S3Exceptions.populate(
            new BucketAlreadyOwnedByYouException(error.additionalProperties().get("BucketName")),
            httpRequestHead, ModelHttpResponseHead.fromResponse(httpResponse), error);
    }
    return null;
  }
}
