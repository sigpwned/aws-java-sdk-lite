package com.sigpwned.aws.sdk.lite.s3.model;

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
public class Delete {
  private List<ObjectIdentifier> objects;
  private Boolean quiet;

  public boolean hasObjects() {
    return objects != null;
  }
}
