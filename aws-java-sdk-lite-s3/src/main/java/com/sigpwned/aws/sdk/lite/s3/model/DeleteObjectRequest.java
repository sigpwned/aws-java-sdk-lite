package com.sigpwned.aws.sdk.lite.s3.model;

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
public class DeleteObjectRequest extends SdkRequest {
  private String bucket;
  private Boolean bypassGovernanceRetention;
  private String expectedBucketOwner;
  private String key;
  private String mfa;
  private String requestPayer;
  private String versionId;
}
