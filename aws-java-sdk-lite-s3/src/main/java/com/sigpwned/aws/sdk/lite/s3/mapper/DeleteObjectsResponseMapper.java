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

import static java.util.Collections.unmodifiableList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectsResponse;
import com.sigpwned.aws.sdk.lite.s3.model.DeletedObject;
import com.sigpwned.aws.sdk.lite.s3.model.S3Error;
import com.sigpwned.aws.sdk.lite.s3.util.XmlHandling;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class DeleteObjectsResponseMapper
    implements ModelHttpBeanClientResponseMapper<DeleteObjectsResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(DeleteObjectsResponse.class);
  }

  @Override
  public DeleteObjectsResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    try {
      String entityText = httpResponse.toString(StandardCharsets.UTF_8);

      System.out.println(entityText);

      // TODO Generic failure?
      if (httpResponse.getStatusCode() != ModelHttpStatusCodes.OK)
        throw new IOException("generic failure");

      Document doc = XmlHandling.parseDocument(entityText);

      DeleteResult deleteResult = deleteResult(doc);

      ModelHttpHeaders hs = httpResponse.getHeaders();
      return DeleteObjectsResponse.builder().deleted(deleteResult.getDeleteds())
          .errors(deleteResult.getErrors())
          .requestCharged(hs.findFirstHeaderByName("x-amz-request-charged").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .build();
    } finally {
      httpResponse.close();
    }
  }

  private static class DeleteResult {
    private List<DeletedObject> deleteds;
    private List<S3Error> errors;

    public DeleteResult(List<DeletedObject> deleteds, List<S3Error> errors) {
      this.deleteds = unmodifiableList(deleteds);
      this.errors = unmodifiableList(errors);
    }

    public List<DeletedObject> getDeleteds() {
      return deleteds;
    }

    public List<S3Error> getErrors() {
      return errors;
    }

    @Override
    public int hashCode() {
      return Objects.hash(deleteds, errors);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      DeleteResult other = (DeleteResult) obj;
      return Objects.equals(deleteds, other.deleteds) && Objects.equals(errors, other.errors);
    }

    @Override
    public String toString() {
      return "DeleteResult [deleteds=" + deleteds + ", errors=" + errors + "]";
    }
  }

  private DeleteResult deleteResult(Document doc) {
    return deleteResult(doc.getDocumentElement());
  }

  /* default */ DeleteResult deleteResult(Element element) {
    if (!element.getNodeName().equals("DeleteResult"))
      throw new IllegalArgumentException("element must have localName DeleteResult");

    List<DeletedObject> deleteds = new ArrayList<>();
    List<S3Error> errors = new ArrayList<>();
    XmlHandling.forEachChildElement(element, childElement -> {
      switch (childElement.getNodeName()) {
        case "Deleted":
          deleteds.add(deletedObject(childElement));
          break;
        case "Error":
          errors.add(error(childElement));
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return new DeleteResult(deleteds, errors);
  }

  private DeletedObject deletedObject(Element element) {
    if (!element.getNodeName().equals("Deleted"))
      throw new IllegalArgumentException("element must have localName Deleted");

    DeletedObject result = new DeletedObject();
    XmlHandling.forEachChildElement(element, childElement -> {
      switch (childElement.getNodeName()) {
        case "Key":
          result.key(childElement.getTextContent());
          break;
        case "DeleteMarker":
          result.deleteMarker(Boolean.parseBoolean(childElement.getTextContent()));
          break;
        case "DeleteMarkerVersionId":
          result.deleteMarkerVersionId(childElement.getTextContent());
          break;
        case "VersionId":
          result.versionId(childElement.getTextContent());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }

  private S3Error error(Element element) {
    if (!element.getNodeName().equals("Error"))
      throw new IllegalArgumentException("element must have localName Error");

    S3Error result = new S3Error();
    XmlHandling.forEachChildElement(element, childElement -> {
      switch (childElement.getNodeName()) {
        case "Key":
          result.key(childElement.getTextContent());
          break;
        case "Code":
          result.code(childElement.getTextContent());
          break;
        case "Message":
          result.message(childElement.getTextContent());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }
}
