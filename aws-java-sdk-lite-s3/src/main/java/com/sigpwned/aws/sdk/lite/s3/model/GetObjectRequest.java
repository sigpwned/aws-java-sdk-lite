package com.sigpwned.aws.sdk.lite.s3.model;

import java.time.Instant;
import com.sigpwned.aws.sdk.lite.core.SdkRequest;
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
public class GetObjectRequest extends SdkRequest {
  private String bucket;
  private String checksumMode;
  private String expectedBucketOwner;
  private String ifMatch;
  private Instant ifModifiedSince;
  private String ifNoneMatch;
  private Instant ifUnmodifiedSince;
  private String key;
  private Integer partNumber;
  private Range range;
  private String requestPayer;
  private String responseCacheControl;
  private String responseContentDisposition;
  private String responseContentEncoding;
  private String responseContentLanguage;
  private String responseContentType;
  private Instant responseExpires;
  private String sseCustomerAlgorithm;
  private String sseCustomerKey;
  private String sseCustomerKeyMD5;
  private String versionId;
}
