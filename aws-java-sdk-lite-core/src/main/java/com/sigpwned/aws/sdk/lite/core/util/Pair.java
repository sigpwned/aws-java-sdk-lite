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

import java.util.function.BiFunction;

/**
 * Simple struct of two values, possibly of different types.
 *
 * @param <LeftT> Left type
 * @param <RightT> Right Type
 */
public final class Pair<LeftT, RightT> {

  private final LeftT left;
  private final RightT right;

  private Pair(LeftT left, RightT right) {
    if (left == null)
      throw new NullPointerException();
    if (right == null)
      throw new NullPointerException();
    this.left = left;
    this.right = right;
  }

  /**
   * @return Left value
   */
  public LeftT left() {
    return this.left;
  }

  /**
   * @return Right value
   */
  public RightT right() {
    return this.right;
  }

  /**
   * Apply the function to both the left and right values and return the transformed result.
   *
   * @param function Function to apply on the {@link Pair}
   * @param <ReturnT> Transformed return type of {@link BiFunction}.
   * @return Result of {@link BiFunction} applied on left and right values of the pair.
   */
  public <ReturnT> ReturnT apply(BiFunction<LeftT, RightT, ReturnT> function) {
    return function.apply(left, right);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Pair)) {
      return false;
    }

    Pair other = (Pair) obj;
    return other.left.equals(left) && other.right.equals(right);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode() + left.hashCode() + right.hashCode();
  }

  @Override
  public String toString() {
    return "Pair(left=" + left + ", right=" + right + ")";
  }

  public static <LeftT, RightT> Pair<LeftT, RightT> of(LeftT left, RightT right) {
    return new Pair<>(left, right);
  }
}
