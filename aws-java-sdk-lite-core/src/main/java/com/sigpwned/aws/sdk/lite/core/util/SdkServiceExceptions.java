package com.sigpwned.aws.sdk.lite.core.util;

import com.sigpwned.aws.sdk.lite.core.SdkServiceException;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public final class SdkServiceExceptions {
  private SdkServiceExceptions() {}

  public static <X extends SdkServiceException> X populate(X exception,
      ModelHttpRequestHead httpRequestHead, ModelHttpResponseHead httpResponseHead,
      ErrorMessage message) {
    exception.extendedRequestId(
        httpResponseHead.getHeaders().findFirstHeaderValueByName("x-amx-id-2").orElse(null));
    exception.requestId(message.requestId());
    exception.statusCode(httpResponseHead.getStatusCode());
    return exception;
  }
}
