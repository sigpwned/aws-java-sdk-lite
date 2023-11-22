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
import com.sigpwned.aws.sdk.lite.s3.model.GetObjectResponse;

public class GetObjectResponseAndObject {
  private final GetObjectResponse response;
  private final ObjectByteSource object;

  public GetObjectResponseAndObject(GetObjectResponse response, ObjectByteSource object) {
    if (response == null)
      throw new NullPointerException();
    if (object == null)
      throw new NullPointerException();
    this.response = response;
    this.object = object;
  }

  public GetObjectResponse getResponse() {
    return response;
  }

  public ObjectByteSource getObject() {
    return object;
  }

  @Override
  public int hashCode() {
    return Objects.hash(object, response);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GetObjectResponseAndObject other = (GetObjectResponseAndObject) obj;
    return Objects.equals(object, other.object) && Objects.equals(response, other.response);
  }

  @Override
  public String toString() {
    return "GetObjectResponsePlusObject [response=" + response + ", object=" + object + "]";
  }
}
