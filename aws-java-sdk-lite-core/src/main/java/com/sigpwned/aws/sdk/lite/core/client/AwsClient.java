package com.sigpwned.aws.sdk.lite.core.client;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import com.sigpwned.aws.sdk.lite.core.AwsEndpoint;
import com.sigpwned.aws.sdk.lite.core.SdkClientException;
import com.sigpwned.aws.sdk.lite.core.credentials.provider.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.http.AwsPropertiesHttpRequestFilter;
import com.sigpwned.aws.sdk.lite.core.http.AwsSigningModelHttpRequestInterceptor;
import com.sigpwned.aws.sdk.lite.core.http.SigV4AwsSigner;
import com.sigpwned.aws.sdk.lite.core.util.AwsEndpoints;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClient;
import com.sigpwned.httpmodel.core.model.ModelHttpAuthority;
import com.sigpwned.httpmodel.core.model.ModelHttpHost;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;
import com.sigpwned.httpmodel.core.util.ModelHttpSchemes;

public abstract class AwsClient implements AutoCloseable {
  private final ModelHttpBeanClient client;
  private final String region;
  private final String serviceName;

  protected AwsClient(ModelHttpBeanClient client, AwsCredentialsProvider credentialsProvider,
      String region, String serviceName) {
    this.client = requireNonNull(client);
    this.region = requireNonNull(region);
    this.serviceName = requireNonNull(serviceName);

    getClient().addRequestFilter(new AwsPropertiesHttpRequestFilter(getServiceName(), getRegion()));
    getClient().addRequestInterceptor(
        new AwsSigningModelHttpRequestInterceptor(new SigV4AwsSigner(credentialsProvider)));
  }

  protected ModelHttpBeanClient getClient() {
    return client;
  }

  protected String getRegion() {
    return region;
  }

  protected String getServiceName() {
    return serviceName;
  }

  /**
   * hook
   */
  protected ModelHttpUrl baseUrl() {
    return ModelHttpUrl.builder().scheme(ModelHttpSchemes.HTTPS)
        .authority(ModelHttpAuthority.of(ModelHttpHost
            .fromString(AwsEndpoints.toHostname(AwsEndpoint.of(getRegion(), getServiceName())))))
        .path("").build();
  }

  @Override
  public void close() {
    unsafe(() -> {
      getClient().close();
    });
  }

  @FunctionalInterface
  protected static interface IOCallable<T> {
    public T call() throws IOException;
  }

  protected static <T> T unsafe(IOCallable<T> callable) {
    try {
      return callable.call();
    } catch (IOException e) {
      throw new SdkClientException(e);
    }
  }

  @FunctionalInterface
  protected static interface IORunnable {
    public void run() throws IOException;
  }

  protected static void unsafe(IORunnable runnable) {
    try {
      runnable.run();
    } catch (IOException e) {
      throw new SdkClientException(e);
    }
  }
}
