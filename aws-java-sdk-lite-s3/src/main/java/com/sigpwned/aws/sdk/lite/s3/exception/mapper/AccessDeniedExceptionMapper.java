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
import com.sigpwned.aws.sdk.lite.s3.exception.AccessDeniedException;
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
 * Forbidden, essentially.
 * </p>
 *
 * <pre>
 *   &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 *   &lt;Error&gt;
 *     &lt;Code&gt;AccessDenied&lt;/Code&gt;
 *     &lt;Message&gt;AWS authentication requires a valid Date or x-amz-date header&lt;/Message&gt;
 *     &lt;RequestId&gt;YC6AT6CP541W1PM8&lt;/RequestId&gt;
 *     &lt;HostId&gt;cZEb2CcB6ixkpnbLrYfltxiouQRLC86wfUdTyAO+P1WQd87l8dv0xHYjWIhNdlgQoXcrQvV/+04=&lt;/HostId&gt;
 *   &lt;/Error&gt;
 * </pre>
 */
public class AccessDeniedExceptionMapper implements ModelHttpBeanClientExceptionMapper {
  // <?xml version="1.0" encoding="UTF-8"?>
  // <Error>
  // <Code>AccessDenied</Code>
  // <Message>AWS authentication requires a valid Date or x-amz-date header</Message>
  // <RequestId>YC6AT6CP541W1PM8</RequestId>
  // <HostId>cZEb2CcB6ixkpnbLrYfltxiouQRLC86wfUdTyAO+P1WQd87l8dv0xHYjWIhNdlgQoXcrQvV/+04=</HostId>
  // </Error>
  @Override
  public AccessDeniedException mapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    if (!httpResponse.hasEntity()) {
      return null;
    }
    if (httpResponse.getStatusCode() == ModelHttpStatusCodes.FORBIDDEN) {
      httpResponse.buffer(InputStreamBufferingStrategy.MEMORY);
      ErrorMessage error = ErrorMessages.fromString(httpResponse.toString(StandardCharsets.UTF_8));
      httpResponse.restart();
      if (error.code().equals(ErrorMessages.CODE_ACCESS_DENIED))
        return S3Exceptions.populate(new AccessDeniedException(), httpRequestHead,
            ModelHttpResponseHead.fromResponse(httpResponse), error);
    }
    return null;
  }
}
