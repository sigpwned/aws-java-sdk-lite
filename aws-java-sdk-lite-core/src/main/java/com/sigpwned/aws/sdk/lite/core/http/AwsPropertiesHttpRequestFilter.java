package com.sigpwned.aws.sdk.lite.core.http;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import com.sigpwned.aws.sdk.lite.core.util.AwsProperties;
import com.sigpwned.httpmodel.core.ModelHttpRequestFilter;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;

public class AwsPropertiesHttpRequestFilter implements ModelHttpRequestFilter {
  private final String serviceName;
  private final String region;

  public AwsPropertiesHttpRequestFilter(String serviceName, String region) {
    this.serviceName = requireNonNull(serviceName);
    this.region = requireNonNull(region);
  }

  @Override
  public void filter(ModelHttpRequestHead requestHead) throws IOException {
    requestHead.setProperty(AwsProperties.AWS_SERVICE_NAME, getServiceName());
    requestHead.setProperty(AwsProperties.AWS_REGION, getRegion());
  }

  private String getServiceName() {
    return serviceName;
  }

  private String getRegion() {
    return region;
  }
}
