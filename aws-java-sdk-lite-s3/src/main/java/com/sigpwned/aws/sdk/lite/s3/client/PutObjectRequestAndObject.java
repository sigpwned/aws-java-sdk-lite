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
package com.sigpwned.aws.sdk.lite.s3.client;

import java.util.Objects;
import com.sigpwned.aws.sdk.lite.core.io.RequestBody;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectRequest;

public class PutObjectRequestAndObject {
  private final PutObjectRequest request;
  private final RequestBody object;

  public PutObjectRequestAndObject(PutObjectRequest request, RequestBody object) {
    if (request == null)
      throw new NullPointerException();
    if (object == null)
      throw new NullPointerException();
    this.request = request;
    this.object = object;
  }

  public PutObjectRequest getRequest() {
    return request;
  }

  public RequestBody getObject() {
    return object;
  }

  @Override
  public int hashCode() {
    return Objects.hash(object, request);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PutObjectRequestAndObject other = (PutObjectRequestAndObject) obj;
    return Objects.equals(object, other.object) && Objects.equals(request, other.request);
  }

  @Override
  public String toString() {
    return "PutObjectRequestAndObject [request=" + request + ", object=" + object + "]";
  }
}
