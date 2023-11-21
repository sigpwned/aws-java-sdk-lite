package com.sigpwned.aws.sdk.lite.s3.util;

import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.aws.sdk.lite.core.util.AwsServiceExceptions;
import com.sigpwned.aws.sdk.lite.s3.S3Exception;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public final class S3Exceptions {
  private S3Exceptions() {}

  public static <X extends S3Exception> X populate(X exception,
      ModelHttpRequestHead httpRequestHead, ModelHttpResponseHead httpResponseHead,
      ErrorMessage message) {
    AwsServiceExceptions.populate(exception, httpRequestHead, httpResponseHead, message);
    return exception;
  }
}
