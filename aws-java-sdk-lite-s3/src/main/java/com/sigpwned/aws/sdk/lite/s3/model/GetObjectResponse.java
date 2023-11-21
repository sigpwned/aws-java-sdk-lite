package com.sigpwned.aws.sdk.lite.s3.model;

import java.time.Instant;
import java.util.Map;
import com.sigpwned.aws.sdk.lite.core.SdkResponse;
import com.sigpwned.aws.sdk.lite.core.model.Range;
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
public class GetObjectResponse extends SdkResponse {
  private String acceptRanges;
  private Boolean bucketKeyEnabled;
  private String cacheControl;
  private String checksumCRC32;
  private String checksumCRC32C;
  private String checksumSHA1;
  private String checksumSHA256;
  private String contentDisposition;
  private String contentEncoding;
  private String contentLanguage;
  private Long contentLength;
  private Range contentRange;
  private String contentType;
  private Boolean deleteMarker;
  private String eTag;
  private String expiration;
  private Instant expires;
  private Instant lastModified;
  private Map<String, String> metadata;
  private Integer missingMeta;
  private String objectLockLegalHoldStatus;
  private String objectLockMode;
  private Instant objectLockRetainUntilDate;
  private Integer partsCount;
  private String replicationStatus;
  private String requestCharged;
  private String restore;
  private String serverSideEncryption;
  private String sseCustomerAlgorithm;
  private String sseCustomerKeyMD5;
  private String ssekmsKeyId;
  private String storageClass;
  private Integer tagCount;
  private String versionId;
  private String websiteRedirectLocation;

  public boolean hasMetadata() {
    return metadata != null;
  }
}
