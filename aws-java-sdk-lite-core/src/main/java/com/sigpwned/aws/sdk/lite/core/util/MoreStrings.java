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
package com.sigpwned.aws.sdk.lite.core.util;

public final class MoreStrings {
  private MoreStrings() {}

  /**
   * If {@code s} is not {@code null} or the empty string, then return {@code s}. Otherwise, return
   * {@code null}.
   */
  public static String notNullOrEmpty(String s) {
    if (s == null)
      return null;
    return s.isEmpty() ? null : s;
  }

  /**
   * If {@code s} is not {@code null} or a string consisting only of whitespace, then return
   * {@code s.trim()}. Otherwise, return {@code null}.
   */
  public static String notNullOrBlank(String s) {
    if (s == null)
      return null;
    return notNullOrEmpty(s.trim());
  }
}
