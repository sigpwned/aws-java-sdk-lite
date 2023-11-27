/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-core
 * ====================================SECTION=====================================
 * Copyright (C) 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.aws.sdk.lite.core.aws;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.net.URI;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.http.AwsPropertiesHttpRequestFilter;
import com.sigpwned.aws.sdk.lite.core.http.AwsSigningHttpRequestInterceptor;
import com.sigpwned.aws.sdk.lite.core.http.SdkResponseDecoratingBeanHttpResponseFilter;
import com.sigpwned.aws.sdk.lite.core.http.SigV4AwsSigner;
import com.sigpwned.aws.sdk.lite.core.sdk.SdkClientException;
import com.sigpwned.httpmodel.client.bean.ModelHttpBeanClient;

public abstract class AwsServiceClientBase implements AwsClient {
  private final ModelHttpBeanClient client;
  private final AwsCredentialsProvider credentialsProvider;
  private final URI endpointOverride;
  private final String region;
  private final String serviceName;

  protected AwsServiceClientBase(ModelHttpBeanClient client,
      AwsCredentialsProvider credentialsProvider, URI endpointOverride, String region,
      String serviceName) {
    this.client = requireNonNull(client);
    this.credentialsProvider = requireNonNull(credentialsProvider);
    this.region = requireNonNull(region);
    this.serviceName = requireNonNull(serviceName);
    this.endpointOverride = endpointOverride;

    getClient().addRequestFilter(new AwsPropertiesHttpRequestFilter(getServiceName(), getRegion()));
    getClient().addBeanResponseFilter(new SdkResponseDecoratingBeanHttpResponseFilter());
    getClient().addRequestInterceptor(
        new AwsSigningHttpRequestInterceptor(new SigV4AwsSigner(credentialsProvider)));
  }

  protected ModelHttpBeanClient getClient() {
    return client;
  }

  protected AwsCredentialsProvider getCredentialsProvider() {
    return credentialsProvider;
  }

  protected URI getEndpointOverride() {
    return endpointOverride;
  }

  protected String getRegion() {
    return region;
  }

  protected String getServiceName() {
    return serviceName;
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
