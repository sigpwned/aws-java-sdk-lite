package com.sigpwned.aws.sdk.lite.s3.mapper;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders.Header;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponse;

public class PutObjectResponseMapper implements ModelHttpBeanClientResponseMapper<PutObjectResponse> {
  @Override
  public boolean isMappable(Class<?> responseType, ModelHttpMediaType contentType) {
    return responseType.equals(PutObjectResponse.class);
  }

  @Override
  public PutObjectResponse mapResponse(ModelHttpRequestHead request, ModelHttpResponse response)
      throws IOException {
    try {
      ModelHttpHeaders hs = response.getHeaders();
      return PutObjectResponse.builder()
          .bucketKeyEnabled(
              hs.findFirstHeaderByName("x-amz-server-side-encryption-bucket-key-enabled")
                  .map(Header::getValue).map(Boolean::valueOf).orElse(null))
          .checksumCRC32(hs.findFirstHeaderByName("x-amz-checksum-crc32").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumCRC32C(hs.findFirstHeaderByName("x-amz-checksum-crc32c").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumSHA1(hs.findFirstHeaderByName("x-amz-checksum-sha1").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .checksumSHA256(hs.findFirstHeaderByName("x-amz-checksum-sha256").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .eTag(hs.findFirstHeaderByName("ETag").map(Header::getValue).map(String::valueOf)
              .orElse(null))
          .requestCharged(hs.findFirstHeaderByName("x-amz-request-charged").map(Header::getValue)
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
          .ssekmsEncryptionContext(hs.findFirstHeaderByName("x-amz-server-side-encryption-context")
              .map(Header::getValue).map(String::valueOf).orElse(null))
          .versionId(hs.findFirstHeaderByName("x-amx-version-id").map(Header::getValue)
              .map(String::valueOf).orElse(null))
          .build();
    } finally {
      response.close();
    }
  }
}
