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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sigpwned.aws.sdk.lite.s3.http.XmlModelHttpEntity;
import com.sigpwned.aws.sdk.lite.s3.model.Delete;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectRequest;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectsRequest;
import com.sigpwned.aws.sdk.lite.s3.model.ObjectIdentifier;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;
import com.sigpwned.millidata.xml.model.Node;
import com.sigpwned.millidata.xml.model.Nodes;
import com.sigpwned.millidata.xml.model.node.Element;
import com.sigpwned.millidata.xml.model.node.Text;

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
        .done().build().toRequest(new XmlModelHttpEntity(delete(request.delete())));
  }

  /* default */ Element delete(Delete delete) {
    // We only need the delete part
    List<Node> children = new ArrayList<>();
    delete.objects().stream().map(this::objectIdentifier).forEach(children::add);
    if (delete.quiet() != null)
      children.add(new Element("Quiet", Nodes.of(new Text(delete.quiet().toString()))));
    return new Element("Delete", Nodes.of(children));
  }

  private Element objectIdentifier(ObjectIdentifier oid) {
    List<Node> children = new ArrayList<>(2);
    children.add(new Element("Key", Nodes.of(new Text(oid.key()))));
    if (oid.versionId() != null)
      children.add(new Element("VersionId", Nodes.of(new Text(oid.versionId()))));
    return new Element("Object", Nodes.of(children));
  }
}
