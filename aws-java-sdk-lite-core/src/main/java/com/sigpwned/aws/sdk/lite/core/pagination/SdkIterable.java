package com.sigpwned.aws.sdk.lite.core.pagination;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface SdkIterable<T> extends Iterable<T> {
  default Stream<T> stream() {
    return StreamSupport.stream(spliterator(), false);
  }
}
