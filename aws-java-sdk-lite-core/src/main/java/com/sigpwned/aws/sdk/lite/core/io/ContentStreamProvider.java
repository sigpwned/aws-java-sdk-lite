package com.sigpwned.aws.sdk.lite.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Not necessarily replayable.
 */
@FunctionalInterface
public interface ContentStreamProvider {
  public InputStream newStream() throws IOException;
}
