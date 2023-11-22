/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-core
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
package com.sigpwned.aws.sdk.lite.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.OptionalLong;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;

public class RequestBodyBufferedInputStream extends BufferedInputStream {
  private final RequestBody requestBody;

  public RequestBodyBufferedInputStream(RequestBody requestBody) {
    if (requestBody == null)
      throw new NullPointerException();
    this.requestBody = requestBody;
  }

  @Override
  public OptionalLong length() throws IOException {
    Long contentLength = getRequestBody().getContentLength();
    return contentLength != null ? OptionalLong.of(contentLength.longValue())
        : OptionalLong.empty();
  }

  @Override
  protected InputStream newInputStream() throws IOException {
    return getRequestBody().getContentStreamProvider().newStream();
  }

  private RequestBody getRequestBody() {
    return requestBody;
  }
}
