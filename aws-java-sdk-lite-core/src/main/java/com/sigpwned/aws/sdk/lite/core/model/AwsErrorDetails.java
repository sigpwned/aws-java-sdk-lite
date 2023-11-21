package com.sigpwned.aws.sdk.lite.core.model;

import com.sigpwned.aws.sdk.lite.core.http.SdkHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public class AwsErrorDetails {
  private String errorCode;
  private String errorMessage;
  private SdkHttpResponse sdkHttpResponse;
  private String serviceName;
}
