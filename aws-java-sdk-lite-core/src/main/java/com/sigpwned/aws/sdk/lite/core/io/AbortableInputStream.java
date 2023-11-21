package com.sigpwned.aws.sdk.lite.core.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import com.sigpwned.aws.sdk.lite.core.Abortable;

public class AbortableInputStream extends FilterInputStream implements Abortable {
  /**
   * Ignores abort
   */
  public static AbortableInputStream create(InputStream delegate) {
    return new AbortableInputStream(delegate, Abortable.NOP);
  }

  public static AbortableInputStream create(InputStream delegate, Abortable abortable) {
    return new AbortableInputStream(delegate, abortable);
  }

  public static AbortableInputStream createEmpty() {
    return create(new NullInputStream());
  }

  private final Abortable abortable;

  public AbortableInputStream(InputStream delegate) {
    super(delegate);
    this.abortable = this;
  }

  public AbortableInputStream(InputStream delegate, Abortable abortable) {
    super(delegate);
    if (abortable == null)
      throw new NullPointerException();
    this.abortable = abortable;
  }

  @Override
  public void abort() {
    getAbortable().abort();
  }

  private Abortable getAbortable() {
    return abortable;
  }
}
