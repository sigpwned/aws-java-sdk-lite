package com.sigpwned.aws.sdk.lite.s3.exception.mapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.aws.sdk.lite.core.util.AwsEndpoints;
import com.sigpwned.aws.sdk.lite.s3.exception.PermanentRedirectS3Exception;
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
 * Request against the wrong region.
 * </p>
 *
 * <pre>
 *   &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 *   &lt;Error&gt;
 *     &lt;Code&gt;PermanentRedirect&lt;/Code&gt;
 *     &lt;Message&gt;The bucket you are attempting to access must be addressed using the specified endpoint. Please send all future requests to this endpoint.&lt;/Message&gt;
 *     &lt;Endpoint&gt;s3.amazonaws.com&lt;/Endpoint&gt;
 *     &lt;Bucket&gt;example&lt;/Bucket&gt;
 *     &lt;RequestId&gt;Y67PM5R99XAGFSYK&lt;/RequestId&gt;
 *     &lt;HostId&gt;ZkbsyjmAlMS33HHWLP8mwwidnpQHQCGXhaSpur3riehWBAjAmqRfFkUe6oRqe27dPIgn2q3srpM=&lt;/HostId&gt;
 *   &lt;/Error&gt;
 * </pre>
 */
public class PermanentRedirectExceptionMapper implements ModelHttpBeanClientExceptionMapper {
  // <?xml version="1.0" encoding="UTF-8"?>
  // <Error>
  // <Code>PermanentRedirect</Code>
  // <Message>The bucket you are attempting to access must be addressed using the specified
  // endpoint. Please send all future requests to this endpoint.</Message>
  // <Endpoint>s3.amazonaws.com</Endpoint>
  // <Bucket>example</Bucket>
  // <RequestId>Y67PM5R99XAGFSYK</RequestId>
  // <HostId>ZkbsyjmAlMS33HHWLP8mwwidnpQHQCGXhaSpur3riehWBAjAmqRfFkUe6oRqe27dPIgn2q3srpM=</HostId>
  // </Error>

  @Override
  public PermanentRedirectS3Exception mapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    if (httpResponse.getStatusCode() == ModelHttpStatusCodes.MOVED_PERMANENTLY) {
      httpResponse.buffer(InputStreamBufferingStrategy.MEMORY);
      ErrorMessage error = ErrorMessages.fromString(httpResponse.toString(StandardCharsets.UTF_8));
      httpResponse.restart();
      if (error.code().equals(ErrorMessages.CODE_PERMANENT_REDIRECT))
        return S3Exceptions.populate(
            new PermanentRedirectS3Exception(
                AwsEndpoints.fromHostname(error.additionalProperties().get("Endpoint"))),
            httpRequestHead, ModelHttpResponseHead.fromResponse(httpResponse), error);
    }
    return null;
  }
}
