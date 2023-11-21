package com.sigpwned.aws.sdk.lite.core.util;

import java.util.Optional;
import com.sigpwned.aws.sdk.lite.core.AwsEndpoint;
import com.sigpwned.aws.sdk.lite.core.AwsServiceException;
import com.sigpwned.aws.sdk.lite.core.http.SdkHttpResponse;
import com.sigpwned.aws.sdk.lite.core.model.AwsErrorDetails;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public final class AwsServiceExceptions {
  private AwsServiceExceptions() {}

  public static <X extends AwsServiceException> X populate(X exception,
      ModelHttpRequestHead httpRequestHead, ModelHttpResponseHead httpResponseHead,
      ErrorMessage message) {
    SdkServiceExceptions.populate(exception, httpRequestHead, httpResponseHead, message);

    AwsEndpoint endpoint;
    try {
      endpoint =
          AwsEndpoints.fromHostname(httpRequestHead.getUrl().getAuthority().getHost().asHostname());
    } catch (Exception e) {
      // That's fine. It's just a custom hostname.
      endpoint = null;
    }

    exception.awsErrorDetails(
        AwsErrorDetails.builder().errorCode(message.code()).errorMessage(message.message())
            .serviceName(Optional.ofNullable(endpoint).map(AwsEndpoint::getService).orElse(null))
            .sdkHttpResponse(SdkHttpResponse.fromModelHttpResponseHead(httpResponseHead)).build());

    return exception;
  }
}
