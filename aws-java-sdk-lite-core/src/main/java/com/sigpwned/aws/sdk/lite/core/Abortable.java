package com.sigpwned.aws.sdk.lite.core;

@FunctionalInterface
public interface Abortable {
  public static final Abortable NOP = () -> {
  };

  public void abort();
}
