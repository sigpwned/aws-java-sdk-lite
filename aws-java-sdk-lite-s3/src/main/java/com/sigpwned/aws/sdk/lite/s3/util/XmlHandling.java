package com.sigpwned.aws.sdk.lite.s3.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlHandling {
  private XmlHandling() {}

  /**
   * Thread safe for our uses, per https://stackoverflow.com/a/29247477/2103602
   */
  private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY =
      DocumentBuilderFactory.newInstance();

  public static DocumentBuilder newDocumentBuilder() {
    try {
      return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new UncheckedIOException(new IOException("Failed to create DocumentBuilder", e));
    }
  }

  public static Document parseDocument(String xml) throws IOException {
    try {
      return newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    } catch (SAXException e) {
      throw new IOException("Failed to parse XML document", e);
    }
  }

  public static void forEachChildNode(Node n, Consumer<Node> consumer) {
    forEachNode(n.getChildNodes(), consumer);
  }

  public static void forEachNode(NodeList ns, Consumer<Node> consumer) {
    for (int i = 0; i < ns.getLength(); i++) {
      Node ni = ns.item(i);
      consumer.accept(ni);
    }
  }

  public static void forEachChildElement(Node n, Consumer<Element> consumer) {
    forEachElement(n.getChildNodes(), consumer);
  }

  public static void forEachElement(NodeList ns, Consumer<Element> consumer) {
    for (int i = 0; i < ns.getLength(); i++) {
      Node ni = ns.item(i);
      if (ni instanceof Element) {
        Element ei = (Element) ni;
        consumer.accept(ei);
      }
    }
  }

  public static Element createTextElement(Document doc, String name, String text) {
    Element result = doc.createElement(name);
    result.appendChild(doc.createTextNode(text));
    return result;
  }

  public static <T> Element createTextElement(Document doc, String name, T value,
      Function<T, String> toString) {
    return createTextElement(doc, name, toString.apply(value));
  }

  public static Element createTextElement(Document doc, String name, Object value) {
    return createTextElement(doc, name, value, Objects::toString);
  }
}
