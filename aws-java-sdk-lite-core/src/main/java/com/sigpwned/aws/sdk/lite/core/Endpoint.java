package com.sigpwned.aws.sdk.lite.core;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Endpoint {
  private URI url;

  @Builder.Default
  private Map<String, List<String>> headers = new HashMap<>();

  /**
   * This diverges from AWS SDK Endpoint design
   */
  @Builder.Default
  private Map<String, Object> properties = new HashMap<>();
}
