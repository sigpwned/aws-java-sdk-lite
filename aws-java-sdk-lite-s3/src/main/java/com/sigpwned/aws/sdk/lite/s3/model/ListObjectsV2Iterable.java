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
package com.sigpwned.aws.sdk.lite.s3.model;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import com.sigpwned.aws.sdk.lite.core.sdk.SdkIterable;
import com.sigpwned.aws.sdk.lite.s3.S3Client;
import com.sigpwned.aws.sdk.lite.s3.model.ListObjectsV2Request.ListObjectsV2RequestBuilder;

public class ListObjectsV2Iterable implements SdkIterable<ListObjectsV2Response> {
  private final S3Client client;
  private final ListObjectsV2Request firstRequest;

  public ListObjectsV2Iterable(S3Client client, ListObjectsV2Request firstRequest) {
    if (client == null)
      throw new NullPointerException();
    if (firstRequest == null)
      throw new NullPointerException();
    this.client = client;
    this.firstRequest = firstRequest;
  }

  public SdkIterable<CommonPrefix> commonPrefixes() {
    return () -> stream().flatMap(r -> r.commonPrefixes().stream()).iterator();

  }

  public SdkIterable<S3Object> contents() {
    return () -> stream().flatMap(r -> r.contents().stream()).iterator();
  }

  @Override
  public Iterator<ListObjectsV2Response> iterator() {
    return new Iterator<ListObjectsV2Response>() {
      private String nextContinuationToken = "";
      private ListObjectsV2Response next = null;

      @Override
      public boolean hasNext() {
        if (next != null)
          return true;
        if (nextContinuationToken == null)
          return false;

        next = fetch(nextContinuationToken);

        nextContinuationToken = next.nextContinuationToken();
        if (Objects.equals(nextContinuationToken, ""))
          nextContinuationToken = null;

        return true;
      }

      @Override
      public ListObjectsV2Response next() {
        if (!hasNext())
          throw new NoSuchElementException();
        ListObjectsV2Response result = next;
        next = null;
        return result;
      }

      private ListObjectsV2Response fetch(String continuationToken) {
        ListObjectsV2RequestBuilder b = getFirstRequest().toBuilder();
        if (continuationToken != null && !continuationToken.equals(""))
          b = b.continuationToken(continuationToken);
        return getClient().listObjectsV2(b.build());
      }
    };
  }

  private S3Client getClient() {
    return client;
  }

  private ListObjectsV2Request getFirstRequest() {
    return firstRequest;
  }
}
