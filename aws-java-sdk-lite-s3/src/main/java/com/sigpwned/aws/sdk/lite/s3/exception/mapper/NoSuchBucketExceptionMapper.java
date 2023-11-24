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
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchBucketException;
import com.sigpwned.aws.sdk.lite.s3.util.ErrorMessages;
import com.sigpwned.aws.sdk.lite.s3.util.S3Exceptions;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientExceptionMapper;
import com.sigpwned.httpmodel.core.io.InputStreamBufferingStrategy;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

/**
 * <p>
 * Request against the wrong region.
 * </p>
 *
 * <pre>
 *   &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;\n
 *     &lt;Error&gt;
 *     &lt;Code&gt;NoSuchBucket&lt;/Code&gt;
 *     &lt;Message&gt;The specified bucket does not exist&lt;/Message&gt;
 *     &lt;BucketName&gt;helckjlaksjel&lt;/BucketName&gt;
 *     &lt;RequestId&gt;JGM6MPYK6AEH1YGA&lt;/RequestId&gt;
 *     &lt;HostId&gt;w8IZIYExBbM/bG9zpSYTJ/dKtqYiAHTXuAncv49Rls4TWWzUTD9RlFkzUlG0QTqFzMvwkrI4VzS5//VqZmqafg==&lt;/HostId&gt;
 *   &lt;/Error&gt;
 * </pre>
 */
public class NoSuchBucketExceptionMapper implements ModelHttpBeanClientExceptionMapper {
  // <?xml version="1.0" encoding="UTF-8"?>\n
  // <Error>
  // <Code>NoSuchBucket</Code>
  // <Message>The specified bucket does not exist</Message>
  // <BucketName>helckjlaksjel</BucketName>
  // <RequestId>JGM6MPYK6AEH1YGA</RequestId>
  // <HostId>w8IZIYExBbM/bG9zpSYTJ/dKtqYiAHTXuAncv49Rls4TWWzUTD9RlFkzUlG0QTqFzMvwkrI4VzS5//VqZmqafg==</HostId>
  // </Error>

  @Override
  public NoSuchBucketException mapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    if (httpRequestHead.getMethod().equals(ModelHttpMethods.HEAD))
      return null;
    if (httpResponse.getStatusCode() == ModelHttpStatusCodes.BAD_REQUEST
        || httpResponse.getStatusCode() == ModelHttpStatusCodes.NOT_FOUND) {
      httpResponse.buffer(InputStreamBufferingStrategy.MEMORY);
      ErrorMessage error = ErrorMessages.fromString(httpResponse.toString(StandardCharsets.UTF_8));
      httpResponse.restart();
      if (error.code().equals(ErrorMessages.CODE_NO_SUCH_BUCKET))
        return S3Exceptions.populate(
            new NoSuchBucketException(error.additionalProperties().get("BucketName")),
            httpRequestHead, ModelHttpResponseHead.fromResponse(httpResponse), error);
    }
    return null;
  }
}
