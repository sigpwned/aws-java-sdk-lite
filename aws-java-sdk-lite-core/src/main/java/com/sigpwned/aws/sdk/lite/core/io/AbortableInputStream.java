/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-core
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
