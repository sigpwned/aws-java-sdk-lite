/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-s3
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
package com.sigpwned.aws.sdk.lite.s3.util;

public final class ObjectStorageClasses {
  private ObjectStorageClasses() {}

  public static final String DEEP_ARCHIVE = "DEEP_ARCHIVE";

  public static final String GLACIER = "GLACIER";

  public static final String GLACIER_IR = "GLACIER_IR";

  public static final String INTELLIGENT_TIERING = "INTELLIGENT_TIERING";

  public static final String ONEZONE_IA = "ONEZONE_IA";

  public static final String OUTPOSTS = "OUTPOSTS";

  public static final String REDUCED_REDUNDANCY = "REDUCED_REDUNDANCY";

  public static final String SNOW = "SNOW";

  public static final String STANDARD = "STANDARD";

  public static final String STANDARD_IA = "STANDARD_IA";
}
