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
