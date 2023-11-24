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
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchKeyException;
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
 *   &lt;Error&gt;
 *     &lt;Code&gt;NoSuchKey&lt;/Code&gt;
 *     &lt;Message&gt;The specified key does not exist.&lt;/Message&gt;
 *     &lt;Key&gt;hello.txt&lt;/Key&gt;
 *     &lt;RequestId&gt;7CJHVA4WCGWCEH6K&lt;/RequestId&gt;
 *     &lt;HostId&gt;7cMUHh/lHF9MfJQEImJbU5OGI2wKVWMJWGoOHZ9L2MtGauD/CkOkCSGV//YFUQW2TeVM6XECXHs=&lt;/HostId&gt;
 *   &lt;/Error&gt;
 * </pre>
 */
public class NoSuchKeyExceptionMapper implements ModelHttpBeanClientExceptionMapper {
  // <?xml version="1.0" encoding="UTF-8"?>\n
  // <Error>
  // <Code>NoSuchKey</Code>
  // <Message>The specified key does not exist.</Message>
  // <Key>hello.txt</Key>
  // <RequestId>7CJHVA4WCGWCEH6K</RequestId>
  // <HostId>7cMUHh/lHF9MfJQEImJbU5OGI2wKVWMJWGoOHZ9L2MtGauD/CkOkCSGV//YFUQW2TeVM6XECXHs=</HostId>
  // </Error>

  @Override
  public NoSuchKeyException mapException(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    if (httpRequestHead.getMethod().equals(ModelHttpMethods.HEAD))
      return null;
    if (httpResponse.getStatusCode() == ModelHttpStatusCodes.BAD_REQUEST
        || httpResponse.getStatusCode() == ModelHttpStatusCodes.NOT_FOUND) {
      httpResponse.buffer(InputStreamBufferingStrategy.MEMORY);
      ErrorMessage error = ErrorMessages.fromString(httpResponse.toString(StandardCharsets.UTF_8));
      httpResponse.restart();
      if (error.code().equals(ErrorMessages.CODE_NO_SUCH_KEY))
        return S3Exceptions.populate(
            new NoSuchKeyException(error.additionalProperties().get("Key")), httpRequestHead,
            ModelHttpResponseHead.fromResponse(httpResponse), error);
    }
    return null;
  }
}
