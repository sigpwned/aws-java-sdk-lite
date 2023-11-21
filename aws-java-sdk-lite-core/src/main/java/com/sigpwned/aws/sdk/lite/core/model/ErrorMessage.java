package com.sigpwned.aws.sdk.lite.core.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public class ErrorMessage {
  private String code;
  private String message;
  private String requestId;
  private String hostId;
  @Singular
  private Map<String, String> additionalProperties;

  public boolean hasAdditionalProperties() {
    return additionalProperties != null;
  }
}
