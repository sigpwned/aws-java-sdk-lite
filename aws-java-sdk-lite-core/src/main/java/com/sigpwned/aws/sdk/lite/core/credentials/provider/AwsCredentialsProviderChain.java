package com.sigpwned.aws.sdk.lite.core.credentials.provider;

import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsCredentials;

public class AwsCredentialsProviderChain implements AwsCredentialsProvider {
  private final List<AwsCredentialsProvider> providers;

  public AwsCredentialsProviderChain(List<AwsCredentialsProvider> providers) {
    if (providers == null)
      throw new NullPointerException();
    if (providers.isEmpty())
      throw new IllegalArgumentException("empty chain");
    this.providers = unmodifiableList(providers);
  }

  @Override
  public AwsCredentials resolveCredentials() {
    for (AwsCredentialsProvider provider : getProviders()) {
      try {
        return provider.resolveCredentials();
      } catch (Exception e) {
        // TODO Should we log? Or something?
        // That's OK, check the rest of the providers
      }
    }
    // TODO What to throw?
    throw new IllegalStateException("no credentials");
  }

  private List<AwsCredentialsProvider> getProviders() {
    return providers;
  }
}
