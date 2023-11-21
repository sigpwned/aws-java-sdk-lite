package com.sigpwned.aws.sdk.lite.core.client;

import com.sigpwned.aws.sdk.lite.core.credentials.provider.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.credentials.provider.DefaultAwsCredentialsProviderChain;
import com.sigpwned.aws.sdk.lite.core.util.AwsRegions;

public abstract class AwsClientBuilder<B extends AwsClientBuilder<B>> {
  private AwsCredentialsProvider credentialsProvider =
      DefaultAwsCredentialsProviderChain.getInstance();
  private String region = AwsRegions.findDefaultRegion().orElse(null);

  public AwsCredentialsProvider credentialsProvider() {
    return credentialsProvider;
  }

  @SuppressWarnings("unchecked")
  public B credentialsProvider(AwsCredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
    return (B) this;
  }

  public String region() {
    return region;
  }

  @SuppressWarnings("unchecked")
  public B region(String region) {
    this.region = region;
    return (B) this;
  }
}
