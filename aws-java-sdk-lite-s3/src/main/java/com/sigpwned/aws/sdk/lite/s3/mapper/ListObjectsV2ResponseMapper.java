package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import com.sigpwned.aws.sdk.lite.s3.model.CommonPrefix;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Response;
import com.sigpwned.aws.sdk.lite.s3.model.S3Object;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;
import com.sigpwned.millidata.xml.XmlReader;
import com.sigpwned.millidata.xml.model.Document;
import com.sigpwned.millidata.xml.model.node.Element;

public class ListObjectsV2ResponseMapper
    implements ModelHttpBeanClientResponseMapper<ListObjectsV2Response> {

  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(ListObjectsV2Response.class);
  }

  @Override
  public ListObjectsV2Response mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse httpResponse) throws IOException {
    try {
      System.out.println(httpResponse.getStatusCode());

      String entityText = httpResponse.toString(StandardCharsets.UTF_8);

      System.out.println(entityText);

      // TODO Generic failure?
      if (httpResponse.getStatusCode() != ModelHttpStatusCodes.OK)
        throw new IOException("generic failure");

      Document doc = new XmlReader(entityText).document();

      return listObjectsV2Response(doc.getRoot());
    } finally {
      httpResponse.close();
    }
  }

  private ListObjectsV2Response listObjectsV2Response(Element root) {
    final ListObjectsV2Response result = new ListObjectsV2Response();
    root.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "CommonPrefixes":
          if (result.commonPrefixes() == null)
            result.commonPrefixes(new ArrayList<>());
          result.commonPrefixes().add(commonPrefix(e));
          break;
        case "Contents":
          if (result.contents() == null)
            result.contents(new ArrayList<>());
          result.contents().add(content(e));
          break;
        case "Delimiter":
          result.delimiter(e.getText());
        case "EncodingType":
          result.encodingType(e.getText());
          break;
        case "KeyCount":
          result.keyCount(Integer.parseInt(e.getText()));
          break;
        case "MaxKeys":
          result.maxKeys(Integer.parseInt(e.getText()));
          break;
        case "Name":
          result.name(e.getText());
          break;
        case "NextContinuationToken":
          result.nextContinuationToken(e.getText());
          break;
        case "Prefix":
          result.prefix(e.getText());
          break;
        case "RequestCharged":
          result.requestCharged(e.getText());
          break;
        case "StartAfter":
          result.startAfter(e.getText());
          break;
        case "IsTruncated":
          result.truncated(Boolean.parseBoolean(e.getText()));
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });
    return result;
  }

  private CommonPrefix commonPrefix(Element root) {
    if (!root.getLocalName().equals("CommonPrefixes"))
      throw new IllegalArgumentException("element must have localName CommonPrefixes");

    final CommonPrefix result = new CommonPrefix();
    root.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "Prefix":
          result.prefix(e.getText());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }

  private S3Object content(Element root) {
    if (!root.getLocalName().equals("Contents"))
      throw new IllegalArgumentException("element must have localName Contents");

    final S3Object result = new S3Object();
    root.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "Key":
          result.key(e.getText());
          break;
        case "LastModified":
          result.lastModified(Instant.parse(e.getText()));
          break;
        case "ETag":
          result.eTag(e.getText());
          break;
        case "Size":
          result.size(Long.parseLong(e.getText()));
          break;
        case "StorageClass":
          result.storageClass(e.getText());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }
}
