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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class RequestBody {
  public static RequestBody empty() {
    return new RequestBody(0L, null, () -> new NullInputStream());
  }

  public static RequestBody fromString(String s, Charset charset) {
    return fromBytes(s.getBytes(charset));
  }

  public static RequestBody fromBytes(byte[] bytes) {
    return new RequestBody(Long.valueOf(bytes.length), null, () -> new ByteArrayInputStream(bytes));
  }

  public static RequestBody fromFile(File file) throws IOException {
    long length = file.length();
    if (length == 0L && !file.isFile())
      throw new NoSuchFileException(file.getPath());
    return new RequestBody(length, null, () -> new FileInputStream(file));
  }

  public static RequestBody fromPath(Path path) throws IOException {
    if (!Files.isRegularFile(path))
      throw new NoSuchFileException(path.toString());
    long length = Files.size(path);
    return new RequestBody(length, null, () -> Files.newInputStream(path));
  }

  private final Long contentLength;
  private final String contentType;
  private final ContentStreamProvider contentStreamProvider;

  public RequestBody(Long contentLength, String contentType,
      ContentStreamProvider contentStreamProvider) {
    if (contentStreamProvider == null)
      throw new NullPointerException();
    this.contentLength = contentLength;
    this.contentType = contentType;
    this.contentStreamProvider = contentStreamProvider;
  }

  public Long getContentLength() {
    return contentLength;
  }

  public String getContentType() {
    return contentType;
  }

  public ContentStreamProvider getContentStreamProvider() {
    return contentStreamProvider;
  }
}
