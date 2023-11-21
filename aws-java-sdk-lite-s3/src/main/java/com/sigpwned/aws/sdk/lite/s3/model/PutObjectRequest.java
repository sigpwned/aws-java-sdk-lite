package com.sigpwned.aws.sdk.lite.s3.model;

import java.time.Instant;
import com.sigpwned.aws.sdk.lite.core.SdkRequest;
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
public class PutObjectRequest extends SdkRequest {
  private String acl;
  private String bucket;
  private Boolean bucketKeyEnabled;
  private String cacheControl;
  private String checksumAlgorithm;
  private String checksumCRC32;
  private String checksumCRC32C;
  private String checksumSHA1;
  private String checksumSHA256;
  private String contentDisposition;
  private String contentEncoding;
  private String contentLanguage;
  private Long contentLength;
  private String contentMD5;
  private String contentType;
  private String expectedBucketOwner;
  private Instant expires;
  private String grantFullControl;
  private String grantRead;
  private String grantReadACP;
  private String grantWriteACP;
  private String key;
  private String objectLockLegalHoldStatus;
  private String objectLockMode;
  private Instant objectLockRetainUntilDate;
  private String requestPayer;
  private String serverSideEncryption;
  private String sseCustomerAlgorithm;
  private String sseCustomerKey;
  private String sseCustomerKeyMD5;
  private String ssekmsEncryptionContext;
  private String ssekmsKeyId;
  private String storageClass;
  private String tagging;
  private String websiteRedirectLocation;
}
