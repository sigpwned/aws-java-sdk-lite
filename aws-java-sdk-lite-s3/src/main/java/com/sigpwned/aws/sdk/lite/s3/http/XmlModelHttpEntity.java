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
