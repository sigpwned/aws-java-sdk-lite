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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sigpwned.aws.sdk.lite.s3.model.CommonPrefix;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Response;
import com.sigpwned.aws.sdk.lite.s3.model.S3Object;
import com.sigpwned.aws.sdk.lite.s3.util.XmlHandling;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

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

      // TODO Generic failure?
      if (httpResponse.getStatusCode() != ModelHttpStatusCodes.OK)
        throw new IOException("generic failure");

      Document doc = XmlHandling.parseDocument(entityText);

      return listObjectsV2Response(doc);
    } finally {
      httpResponse.close();
    }
  }

  /* default */ ListObjectsV2Response listObjectsV2Response(Document doc) {
    return listObjectsV2Response(doc.getDocumentElement());
  }

  private ListObjectsV2Response listObjectsV2Response(Element element) {
    final ListObjectsV2Response result = new ListObjectsV2Response();

    XmlHandling.forEachChildElement(element, childElement -> {
      switch (childElement.getNodeName()) {
        case "CommonPrefixes":
          if (result.commonPrefixes() == null)
            result.commonPrefixes(new ArrayList<>());
          result.commonPrefixes().add(commonPrefix(childElement));
          break;
        case "Contents":
          if (result.contents() == null)
            result.contents(new ArrayList<>());
          result.contents().add(content(childElement));
          break;
        case "Delimiter":
          result.delimiter(childElement.getTextContent());
        case "EncodingType":
          result.encodingType(childElement.getTextContent());
          break;
        case "KeyCount":
          result.keyCount(Integer.parseInt(childElement.getTextContent()));
          break;
        case "MaxKeys":
          result.maxKeys(Integer.parseInt(childElement.getTextContent()));
          break;
        case "Name":
          result.name(childElement.getTextContent());
          break;
        case "NextContinuationToken":
          result.nextContinuationToken(childElement.getTextContent());
          break;
        case "Prefix":
          result.prefix(childElement.getTextContent());
          break;
        case "RequestCharged":
          result.requestCharged(childElement.getTextContent());
          break;
        case "StartAfter":
          result.startAfter(childElement.getTextContent());
          break;
        case "IsTruncated":
          result.truncated(Boolean.parseBoolean(childElement.getTextContent()));
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });
    return result;
  }

  private CommonPrefix commonPrefix(Element element) {
    if (!element.getNodeName().equals("CommonPrefixes"))
      throw new IllegalArgumentException("element must have localName CommonPrefixes");

    final CommonPrefix result = new CommonPrefix();
    XmlHandling.forEachChildElement(element, childElement -> {
      switch (childElement.getNodeName()) {
        case "Prefix":
          result.prefix(childElement.getTextContent());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }

  private S3Object content(Element element) {
    if (!element.getNodeName().equals("Contents"))
      throw new IllegalArgumentException("element must have localName Contents");

    final S3Object result = new S3Object();
    XmlHandling.forEachChildElement(element, childElement -> {
      switch (childElement.getNodeName()) {
        case "Key":
          result.key(childElement.getTextContent());
          break;
        case "LastModified":
          result.lastModified(Instant.parse(childElement.getTextContent()));
          break;
        case "ETag":
          result.eTag(childElement.getTextContent());
          break;
        case "Size":
          result.size(Long.parseLong(childElement.getTextContent()));
          break;
        case "StorageClass":
          result.storageClass(childElement.getTextContent());
          break;
        default:
          // Ignore anything we don't recognize...
          break;
      }
    });

    return result;
  }
}
