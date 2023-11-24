/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-s3
 * ====================================SECTION=====================================
 * Copyright (C) 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.aws.sdk.lite.s3.exception.mapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.aws.sdk.lite.core.util.AwsEndpoints;
import com.sigpwned.aws.sdk.lite.s3.exception.PermanentRedirectS3Exception;
import com.sigpwned.aws.sdk.lite.s3.util.ErrorMessages;
import com.sigpwned.aws.sdk.lite.s3.util.S3Exceptions;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientExceptionMapper;
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
