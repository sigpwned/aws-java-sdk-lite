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

import static java.lang.String.format;
import java.io.IOException;
import java.util.Optional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sigpwned.aws.sdk.lite.s3.http.XmlModelHttpEntity;
import com.sigpwned.aws.sdk.lite.s3.model.Delete;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectsRequest;
import com.sigpwned.aws.sdk.lite.s3.model.ObjectIdentifier;
import com.sigpwned.aws.sdk.lite.s3.util.XmlHandling;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class DeleteObjectsRequestMapper
    implements ModelHttpBeanClientRequestMapper<DeleteObjectsRequest> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(DeleteObjectRequest.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      DeleteObjectsRequest request) throws IOException {
    return httpRequestHead.toBuilder().method(ModelHttpMethods.POST).url().queryString()
        .setOnlyParameter("delete", "").done().appendPath(format("%s/", request.bucket())).done()
        .headers()
        .setOnlyHeader("x-amz-bypass-governance-retention",
            Optional.ofNullable(request.bypassGovernanceRetention()).map(Object::toString)
                .orElse(null))
        .setOnlyHeader("x-amz-expected-bucket-owner",
            Optional.ofNullable(request.expectedBucketOwner()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-mfa",
            Optional.ofNullable(request.mfa()).map(Object::toString).orElse(null))
        .setOnlyHeader("x-amz-request-payer",
            Optional.ofNullable(request.requestPayer()).map(Object::toString).orElse(null))
        .done().build().toRequest(new XmlModelHttpEntity(deleteDocument(request.delete())));
  }

  /* default */ Document deleteDocument(Delete value) {
    Document doc = newDocument();

    doc.appendChild(delete(doc, value));

    return doc;
  }

  private Element delete(Document doc, Delete delete) {
    Element result = doc.createElement("Delete");
    delete.objects().stream().map(o -> objectIdentifier(doc, o)).forEach(result::appendChild);
    if (delete.quiet() != null) {
      result.appendChild(XmlHandling.createTextElement(doc, "Quiet", delete.quiet()));
    }
    return result;
  }

  private Element objectIdentifier(Document doc, ObjectIdentifier oid) {
    Element result = doc.createElement("Object");
    result.appendChild(XmlHandling.createTextElement(doc, "Key", oid.key()));
    if (oid.versionId() != null)
      result.appendChild(XmlHandling.createTextElement(doc, "VersionId", oid.versionId()));
    return result;
  }

  /**
   * hook
   */
  /* default */ Document newDocument() {
    return XmlHandling.newDocumentBuilder().newDocument();
  }
}
