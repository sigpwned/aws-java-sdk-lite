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
package com.sigpwned.aws.sdk.lite.s3.http;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import com.sigpwned.httpmodel.core.io.BufferedInputStream;
import com.sigpwned.httpmodel.core.io.buffered.MemoryBufferedInputStream;
import com.sigpwned.httpmodel.core.model.ModelHttpEntity;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.millidata.xml.XmlWriter;
import com.sigpwned.millidata.xml.model.Document;
import com.sigpwned.millidata.xml.model.node.Element;

public class XmlModelHttpEntity implements ModelHttpEntity {
  private final Document document;

  public XmlModelHttpEntity(Element root) {
    this(new Document(root));
  }

  public XmlModelHttpEntity(Document document) {
    if (document == null)
      throw new NullPointerException();
    this.document = document;
  }

  private static final ModelHttpMediaType APPLICATION_XML =
      ModelHttpMediaType.of("application", "xml").withCharset(StandardCharsets.UTF_8);

  @Override
  public ModelHttpMediaType getContentType() {
    return APPLICATION_XML;
  }

  @Override
  public BufferedInputStream toInputStream() throws IOException {
    StringWriter buf = new StringWriter();
    try {
      new XmlWriter(buf).document(getDocument());
    } finally {
      buf.close();
    }
    return MemoryBufferedInputStream.of(buf.toString(), StandardCharsets.UTF_8);
  }

  private Document getDocument() {
    return document;
  }
}
