package com.sigpwned.aws.sdk.lite.s3.model;

import java.time.Instant;
import com.sigpwned.aws.sdk.lite.core.SdkResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(fluent = true, chain = true)
public class PutObjectResponse extends SdkResponse {
  private Boolean bucketKeyEnabled;
  private String checksumCRC32;
  private String checksumCRC32C;
  private String checksumSHA1;
  private String checksumSHA256;
  private String eTag;
  private Instant expiration;
  private String requestCharged;
  private String serverSideEncryption;
  private String sseCustomerAlgorithm;
  private String sseCustomerKeyMD5;
  private String ssekmsEncryptionContext;
  private String ssekmsKeyId;
  private String versionId;
}
