package com.sigpwned.aws.sdk.lite.s3.model;

import java.util.List;
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
public class ListObjectsV2Request extends SdkRequest {
  private String bucket;
  private String continuationToken;
  private String delimiter;
  private String encodingType;
  private String expectedBucketOwner;
  private Boolean fetchOwner;
  private Integer maxKeys;
  private List<String> optionalObjectAttributes;
  private String prefix;
  private String requestPayer;
  private String startAfter;

  public boolean hasOptionalObjectAttributes() {
    return optionalObjectAttributes != null;
  }
}
