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
package com.sigpwned.aws.sdk.lite.core.auth.credentials.provider;

import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentialsProvider;

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
