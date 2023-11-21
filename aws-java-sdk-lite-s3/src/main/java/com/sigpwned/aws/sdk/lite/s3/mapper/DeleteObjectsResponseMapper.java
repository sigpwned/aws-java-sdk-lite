package com.sigpwned.aws.sdk.lite.s3.mapper;

import static java.util.Collections.unmodifiableList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.sigpwned.aws.sdk.lite.s3.model.DeleteObjectsResponse;
import com.sigpwned.aws.sdk.lite.s3.model.DeletedObject;
import com.sigpwned.aws.sdk.lite.s3.model.S3Error;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;
import com.sigpwned.millidata.xml.XmlReader;
import com.sigpwned.millidata.xml.model.Document;
import com.sigpwned.millidata.xml.model.node.Element;

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

      Document document = new XmlReader(entityText).document();

      DeleteResult deleteResult = deleteResult(document.getRoot());

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

  /* default */ DeleteResult deleteResult(Element element) {
    if (!element.getLocalName().equals("DeleteResult"))
      throw new IllegalArgumentException("element must have localName DeleteResult");

    List<DeletedObject> deleteds = new ArrayList<>();
    List<S3Error> errors = new ArrayList<>();
    element.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "Deleted":
          deleteds.add(deletedObject(e));
          break;
        case "Error":
          errors.add(error(e));
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return new DeleteResult(deleteds, errors);
  }

  private DeletedObject deletedObject(Element element) {
    if (!element.getLocalName().equals("Deleted"))
      throw new IllegalArgumentException("element must have localName Deleted");

    DeletedObject result = new DeletedObject();
    element.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "Key":
          result.key(e.getText());
          break;
        case "DeleteMarker":
          result.deleteMarker(Boolean.parseBoolean(e.getText()));
          break;
        case "DeleteMarkerVersionId":
          result.deleteMarkerVersionId(e.getText());
          break;
        case "VersionId":
          result.versionId(e.getText());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }

  private S3Error error(Element element) {
    if (!element.getLocalName().equals("Error"))
      throw new IllegalArgumentException("element must have localName Error");

    S3Error result = new S3Error();
    element.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "Key":
          result.key(e.getText());
          break;
        case "Code":
          result.code(e.getText());
          break;
        case "Message":
          result.message(e.getText());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }
}
