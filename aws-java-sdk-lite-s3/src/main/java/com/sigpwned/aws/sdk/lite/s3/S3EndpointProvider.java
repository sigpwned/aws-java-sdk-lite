package com.sigpwned.aws.sdk.lite.s3;

import java.net.URI;
import com.sigpwned.aws.sdk.lite.core.AwsEndpoint;
import com.sigpwned.aws.sdk.lite.core.Endpoint;
import com.sigpwned.aws.sdk.lite.core.util.AwsEndpoints;
import com.sigpwned.aws.sdk.lite.s3.util.S3;

@FunctionalInterface
public interface S3EndpointProvider {
  public static S3EndpointProvider defaultProvider() {
    return params -> Endpoint.builder()
        .url(URI.create(
            "https://" + AwsEndpoints.toHostname(AwsEndpoint.of(params.region(), S3.SERVICE_NAME))))
        .build();
  }

  public Endpoint resolveEndpoint(S3EndpointParams params);
}
