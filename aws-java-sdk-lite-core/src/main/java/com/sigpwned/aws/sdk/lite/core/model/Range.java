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
package com.sigpwned.aws.sdk.lite.core.model;

import java.util.Objects;
import java.util.Optional;

public class Range {
  public static Range fromString(String s) {
    if (s == null)
      throw new NullPointerException();
    String[] parts = s.trim().split("-", 3);
    if (parts.length != 2)
      throw new IllegalArgumentException("invalid range");
    Long from = Optional.of(parts[0]).filter(p -> !p.isEmpty()).map(Long::parseLong).orElse(null);
    Long to = Optional.of(parts[1]).filter(p -> !p.isEmpty()).map(Long::parseLong).orElse(null);
    return new Range(from, to);
  }

  private Long from;
  private Long to;

  public Range() {}

  public Range(Long from, Long to) {
    this.from = from;
    this.to = to;
  }

  public Long getFrom() {
    return from;
  }

  public void setFrom(Long from) {
    this.from = from;
  }

  public Long getTo() {
    return to;
  }

  public void setTo(Long to) {
    this.to = to;
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Range other = (Range) obj;
    return Objects.equals(from, other.from) && Objects.equals(to, other.to);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (from != null)
      result.append(from);
    result.append("-");
    if (to != null)
      result.append(to);
    return result.toString();
  }
}
