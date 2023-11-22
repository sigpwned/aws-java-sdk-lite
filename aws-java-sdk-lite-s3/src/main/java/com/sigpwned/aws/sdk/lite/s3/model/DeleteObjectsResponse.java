package com.sigpwned.aws.sdk.lite.s3.model;

import java.util.ArrayList;
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
public class DeleteObjectsResponse extends SdkResponse {
  @Builder.Default
  private List<DeletedObject> deleted = new ArrayList<>();
  @Builder.Default
  private List<S3Error> errors = new ArrayList<>();
  private String requestCharged;

  public boolean hasDeleted() {
    return deleted != null;
  }

  public boolean hasErrors() {
    return errors != null;
  }
}
