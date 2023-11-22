package com.sigpwned.aws.sdk.lite.s3.model;

import java.util.ArrayList;
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
  @Builder.Default
  private List<ObjectIdentifier> objects = new ArrayList<>();
  private Boolean quiet;

  public boolean hasObjects() {
    return objects != null;
  }
}
