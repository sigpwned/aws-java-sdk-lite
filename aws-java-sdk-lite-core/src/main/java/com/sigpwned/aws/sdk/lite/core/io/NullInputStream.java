package com.sigpwned.aws.sdk.lite.core.io;

import java.io.IOException;
import java.io.InputStream;

public class NullInputStream extends InputStream {
  @Override
  public int read() throws IOException {
    return -1;
  }
}
