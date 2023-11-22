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

import java.io.FilterInputStream;
import java.io.InputStream;
import com.sigpwned.aws.sdk.lite.core.Abortable;

public class ResponseInputStream<ResponseT> extends FilterInputStream implements Abortable {
  private final Abortable abortable;
  private ResponseT response;

  public ResponseInputStream(ResponseT response, InputStream in) {
    this(response, AbortableInputStream.create(in));
  }

  public ResponseInputStream(ResponseT response, AbortableInputStream in) {
    super(in);
    if (response == null)
      throw new NullPointerException();
    this.abortable = in;
    this.response = response;
  }

  public ResponseT getResponse() {
    return response;
  }

  public void setResponse(ResponseT response) {
    this.response = response;
  }

  private Abortable getAbortable() {
    return abortable;
  }

  @Override
  public void abort() {
    getAbortable().abort();
  }
}
