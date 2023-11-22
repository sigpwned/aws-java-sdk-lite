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
package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import com.sigpwned.aws.sdk.lite.s3.exception.NoSuchKeyException;
import com.sigpwned.aws.sdk.lite.s3.model.HeadObjectResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;
import com.sigpwned.httpmodel.core.util.ModelHttpStatusCodes;

public class HeadObjectResponseMapper
    implements ModelHttpBeanClientResponseMapper<HeadObjectResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(HeadObjectResponse.class);
  }

  @Override
  public HeadObjectResponse mapResponse(ModelHttpRequestHead httpRequestHead,
      ModelHttpResponse response) throws IOException {
    try {
      @SuppressWarnings("unused")
      String bucket = httpRequestHead.getProperty(HeadObjectRequestMapper.X_S3_BUCKET_PROPERTY_NAME)
          .map(String.class::cast).get();
      String key = httpRequestHead.getProperty(HeadObjectRequestMapper.X_S3_KEY_PROPERTY_NAME)
          .map(String.class::cast).get();

      // As documented
      // https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadObject.html#API_HeadObject_Errors
      // We also see 404 in the wild, which makes way more sense, if we're honest.
      if (response.getStatusCode() == ModelHttpStatusCodes.BAD_REQUEST
          || response.getStatusCode() == ModelHttpStatusCodes.NOT_FOUND)
        throw new NoSuchKeyException(key);

      ModelHttpHeaders hs = response.getHeaders();
      return HeadObjectResponse.builder()
          .acceptRanges(hs.findFirstHeaderByName("accept-ranges").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .bucketKeyEnabled(
              hs.findFirstHeaderByName("x-amz-server-side-encryption-bucket-key-enabled")
                  .map(Header::getValue).map(Boolean::valueOf).orElse(null))
          .cacheControl(hs.findFirstHeaderByName("cache-control").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumCRC32(hs.findFirstHeaderByName("x-amz-checksum-crc32").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumCRC32C(hs.findFirstHeaderByName("x-amz-checksum-crc32c").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumSHA1(hs.findFirstHeaderByName("x-amz-checksum-sha1").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumSHA256(hs.findFirstHeaderByName("x-amz-checksum-sha256").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .contentDisposition(hs.findFirstHeaderByName("content-disposition").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .contentEncoding(hs.findFirstHeaderByName("content-encoding").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .contentLanguage(hs.findFirstHeaderByName("content-language").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .contentLength(hs.findFirstHeaderByName("content-length").map(Header::getValue)
              .map(Long::parseLong).orElse(null))
          .contentType(hs.findFirstHeaderByName("content-type").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .deleteMarker(hs.findFirstHeaderByName("x-amz-delete-marker").map(Header::getValue)
              .map(Boolean::valueOf).orElse(null))
          .eTag(hs.findFirstHeaderByName("etag").map(Header::getValue).map(String::valueOf)
              .orElse(null))
          .expiration(hs.findFirstHeaderByName("x-amz-expiration").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .expires(hs.findFirstHeaderByName("expires").map(Header::getValue)
              .map(s -> OffsetDateTime.parse(s, DateTimeFormatter.RFC_1123_DATE_TIME))
              .map(OffsetDateTime::toInstant).orElse(null))
          .lastModified(hs.findFirstHeaderByName("last-modified").map(Header::getValue)
              .map(s -> OffsetDateTime.parse(s, DateTimeFormatter.RFC_1123_DATE_TIME))
              .map(OffsetDateTime::toInstant).orElse(null))
          .missingMeta(hs.findFirstHeaderByName("x-amz-missing-meta").map(Header::getValue)
              .map(Integer::valueOf).orElse(null))
          .objectLockLegalHoldStatus(hs.findFirstHeaderByName("x-amz-object-lock-legal-hold")
              .map(Header::getValue).map(String::valueOf).orElse(null))
          .objectLockMode(hs.findFirstHeaderByName("x-amz-object-lock-mode").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .objectLockRetainUntilDate(
              hs.findFirstHeaderByName("x-amz-object-lock-retain-until-date").map(Header::getValue)
                  .map(s -> OffsetDateTime.parse(s, DateTimeFormatter.RFC_1123_DATE_TIME))
                  .map(OffsetDateTime::toInstant).orElse(null))
          .partsCount(hs.findFirstHeaderByName("x-amz-mp-parts-count").map(Header::getValue)
              .map(Integer::valueOf).orElse(null))
          .replicationStatus(hs.findFirstHeaderByName("x-amz-replication-status")
              .map(Header::getValue).map(String::valueOf).orElse(null))
          .requestCharged(hs.findFirstHeaderByName("x-amz-request-charged").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .restore(hs.findFirstHeaderByName("x-amz-restore").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .serverSideEncryption(hs.findFirstHeaderByName("x-amz-server-side-encryption")
              .map(Header::getValue).map(String::valueOf).orElse(null))
          .ssekmsKeyId(hs.findFirstHeaderByName("x-amz-server-side-encryption-aws-kms-key-id")
              .map(Header::getValue).map(String::valueOf).orElse(null))
          .sseCustomerAlgorithm(
              hs.findFirstHeaderByName("x-amz-server-side-encryption-customer-algorithm")
                  .map(Header::getValue).map(String::valueOf).orElse(null))
          .sseCustomerKeyMD5(
              hs.findFirstHeaderByName("x-amz-server-side-encryption-customer-key-MD5")
                  .map(Header::getValue).map(String::valueOf).orElse(null))
          .storageClass(hs.findFirstHeaderByName("x-amz-storage-class").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .versionId(hs.findFirstHeaderByName("x-amx-version-id").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .websiteRedirectLocation(hs.findFirstHeaderByName("x-amz-website-redirect-location")
              .map(String::valueOf).orElse(null))
          .build();
    } finally {
      response.close();
    }
  }
}
