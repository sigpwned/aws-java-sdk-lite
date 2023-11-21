package com.sigpwned.aws.sdk.lite.s3.model;

import java.util.List;
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
public class ListObjectsV2Response extends SdkResponse {
  private List<CommonPrefix> commonPrefixes;
  private List<S3Object> contents;
  private String continuationToken;
  private String delimiter;
  private String encodingType;
  private Boolean truncated;
  private Integer keyCount;
  private Integer maxKeys;
  private String name;
  private String nextContinuationToken;
  private String prefix;
  private String requestCharged;
  private String startAfter;

  public boolean hasCommonPrefixes() {
    return commonPrefixes != null;
  }

  public boolean hasContents() {
    return contents != null;
  }
}
