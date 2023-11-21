package com.sigpwned.aws.sdk.lite.s3.client;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.core.io.AbortableInputStream;

public interface ObjectByteSource extends AutoCloseable {
  public AbortableInputStream getBytes() throws IOException;


  /**
   * Free underlying storage
   */
  @Override
  public void close() throws IOException;
}
