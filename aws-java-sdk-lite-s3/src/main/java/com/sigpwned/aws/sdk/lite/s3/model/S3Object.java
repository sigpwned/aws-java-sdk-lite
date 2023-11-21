package com.sigpwned.aws.sdk.lite.s3.model;

import java.time.Instant;
import java.util.List;
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
public class S3Object {
  private List<String> checksumAlgorithm;
  private String eTag;
  private String key;
  private Instant lastModified;
  private Owner owner;
  private RestoreStatus restoreStatus;
  private Long size;
  private String storageClass;

  public boolean hasChecksumAlgorithm() {
    return checksumAlgorithm != null;
  }
}
