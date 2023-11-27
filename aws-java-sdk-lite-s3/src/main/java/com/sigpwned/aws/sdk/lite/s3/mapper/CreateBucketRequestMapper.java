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
package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import java.util.Optional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sigpwned.aws.sdk.lite.s3.http.XmlModelHttpEntity;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketConfiguration;
import com.sigpwned.aws.sdk.lite.s3.model.CreateBucketRequest;
import com.sigpwned.aws.sdk.lite.s3.util.XmlHandling;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class CreateBucketRequestMapper
    implements ModelHttpBeanClientRequestMapper<CreateBucketRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(CreateBucketRequest.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      CreateBucketRequest request) throws IOException {
    return httpRequestHead.toBuilder().method(ModelHttpMethods.PUT).url()
        .appendPath(request.bucket()).done().headers()
        .setOnlyHeader("x-amz-acl",
            Optional.ofNullable(request.acl()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-grant-full-control",
            Optional.ofNullable(request.grantFullControl()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-grant-read",
            Optional.ofNullable(request.grantRead()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-grant-read-acp",
            Optional.ofNullable(request.grantReadACP()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-grant-write",
            Optional.ofNullable(request.grantWrite()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-grant-write-acp",
            Optional.ofNullable(request.grantWriteACP()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-bucket-object-lock-enabled",
            Optional.ofNullable(request.objectLockEnabledForBucket()).map(Object::toString)
                .orElse(null))
        .setOnlyHeader("x-amz-object-ownership",
            Optional.ofNullable(request.objectOwnership()).map(Object::toString).orElse(null))
        .done().build().toRequest(new XmlModelHttpEntity(
            createBucketConfigurationDocument(request.createBucketConfiguration())));
  }

  /* default */ Document createBucketConfigurationDocument(CreateBucketConfiguration value) {
    Document doc = newDocument();

    doc.appendChild(createBucketConfiguration(doc, value));

    return doc;
  }

  private Element createBucketConfiguration(Document doc, CreateBucketConfiguration value) {
    Element result = doc.createElement("CreateBucketConfiguration");

    result.setAttribute("xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");

    if (value != null && value.locationConstraint() != null) {
      result.appendChild(
          XmlHandling.createTextElement(doc, "LocationConstraint", value.locationConstraint()));
    }

    return result;
  }

  /**
   * hook
   */
  /* default */ Document newDocument() {
    return XmlHandling.newDocumentBuilder().newDocument();
  }
}
