/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-s3
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
package com.sigpwned.aws.sdk.lite.client;

import java.net.URI;
import com.sigpwned.aws.sdk.lite.S3ClientTest;
import com.sigpwned.aws.sdk.lite.core.credentials.AwsCredentials;
import com.sigpwned.aws.sdk.lite.s3.S3Client;
import com.sigpwned.aws.sdk.lite.s3.client.DefaultS3Client;
import com.sigpwned.httpmodel.core.model.ModelHttpUrl;

public class DefaultS3ClientTest extends S3ClientTest {
  @Override
  protected S3Client newClient(String endpoint, AwsCredentials credentials, String region) {
    return new DefaultS3Client(() -> credentials, region) {
      @Override
      protected ModelHttpUrl baseUrl() {
        return ModelHttpUrl.fromUri(URI.create(endpoint));
      }
    };
  }
}
