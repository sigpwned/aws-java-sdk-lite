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
package com.sigpwned.aws.sdk.lite.core;

public abstract class SdkServiceException extends SdkException {
  private static final long serialVersionUID = 1089432262818299913L;

  private int statusCode;
  private String requestId;
  private String extendedRequestId;

  public SdkServiceException(String message) {
    super(message);
  }

  public int statusCode() {
    return statusCode;
  }

  public void statusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String requestId() {
    return requestId;
  }

  public void requestId(String requestId) {
    this.requestId = requestId;
  }

  public String extendedRequestId() {
    return extendedRequestId;
  }

  public void extendedRequestId(String extendedRequestId) {
    this.extendedRequestId = extendedRequestId;
  }
}
