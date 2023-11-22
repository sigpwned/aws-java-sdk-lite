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

public class ResponseBody {
  private final Long contentLength;
  private final String contentType;
  private final ContentStreamProvider contentStreamProvider;

  public ResponseBody(Long contentLength, String contentType,
      ContentStreamProvider contentStreamProvider) {
    if (contentStreamProvider == null)
      throw new NullPointerException();
    this.contentLength = contentLength;
    this.contentType = contentType;
    this.contentStreamProvider = contentStreamProvider;
  }

  public Long getContentLength() {
    return contentLength;
  }

  public String getContentType() {
    return contentType;
  }

  public ContentStreamProvider getContentStreamProvider() {
    return contentStreamProvider;
  }
}
