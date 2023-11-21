package com.sigpwned.aws.sdk.lite.s3.util;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage;
import com.sigpwned.aws.sdk.lite.core.model.ErrorMessage.ErrorMessageBuilder;
import com.sigpwned.millidata.xml.XmlReader;
import com.sigpwned.millidata.xml.model.Document;
import com.sigpwned.millidata.xml.model.node.Element;

public final class ErrorMessages {
  private ErrorMessages() {}

  public static final String CODE_ACCESS_DENIED = "AccessDenied";

  public static final String CODE_PERMANENT_REDIRECT = "PermanentRedirect";

  public static final String CODE_EXPIRED_TOKEN = "ExpiredToken";

  public static final String CODE_NO_SUCH_BUCKET = "NoSuchBucket";

  public static final String CODE_NO_SUCH_KEY = "NoSuchKey";

  public static ErrorMessage fromString(String xml) throws IOException {
    return fromXml(new XmlReader(xml).document());
  }

  public static ErrorMessage fromXml(Document doc) {
    return fromXml(doc.getRoot());
  }

  public static ErrorMessage fromXml(Element root) {
    if (!root.getLocalName().equals("Error"))
      throw new IllegalArgumentException("element must have localName Error");

    final ErrorMessageBuilder result = ErrorMessage.builder();
    root.getChildren().elements().forEach(e -> {
      switch (e.getLocalName()) {
        case "Code":
          result.code(e.getText());
          break;
        case "Message":
          result.message(e.getText());
          break;
        case "RequestId":
          result.requestId(e.getText());
          break;
        case "HostId":
          result.hostId(e.getText());
          break;
        default:
          // Everything else goes into additionalProperties
          result.additionalProperty(e.getLocalName(), e.getText());
          break;
      }
    });

    return result.build();
  }
}
