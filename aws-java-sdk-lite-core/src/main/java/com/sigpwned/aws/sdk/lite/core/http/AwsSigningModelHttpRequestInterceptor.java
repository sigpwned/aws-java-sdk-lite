package com.sigpwned.aws.sdk.lite.core.http;

import java.io.IOException;
import com.sigpwned.aws.sdk.lite.core.AwsSigner;
import com.sigpwned.httpmodel.core.ModelHttpRequestInterceptor;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;

public class AwsSigningModelHttpRequestInterceptor implements ModelHttpRequestInterceptor {
  private final AwsSigner signer;

  public AwsSigningModelHttpRequestInterceptor(AwsSigner signer) {
    if (signer == null)
      throw new NullPointerException();
    this.signer = signer;
  }

  @Override
  public void intercept(ModelHttpRequest request) throws IOException {
    getSigner().sign(request);
  }

  private AwsSigner getSigner() {
    return signer;
  }
}
