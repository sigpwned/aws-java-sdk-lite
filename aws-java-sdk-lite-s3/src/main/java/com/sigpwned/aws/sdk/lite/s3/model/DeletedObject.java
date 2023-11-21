package com.sigpwned.aws.sdk.lite.s3.model;

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
public class DeletedObject {
  private Boolean deleteMarker;
  private String deleteMarkerVersionId;
  private String key;
  private String versionId;
}
