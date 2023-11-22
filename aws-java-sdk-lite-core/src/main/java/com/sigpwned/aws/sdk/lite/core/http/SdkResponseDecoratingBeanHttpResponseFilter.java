package com.sigpwned.aws.sdk.lite.core.http;

import com.sigpwned.aws.sdk.lite.core.SdkResponse;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientResponseFilter;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.model.ModelHttpResponseHead;

public class SdkResponseDecoratingBeanHttpResponseFilter implements ModelHttpBeanClientResponseFilter {
  @Override
  public void filter(ModelHttpRequestHead httpRequestHead, Object requestBean,
      ModelHttpResponseHead httpResponseHead, Object responseBean) {
    if (responseBean != null && responseBean instanceof SdkResponse) {
      SdkResponse sdkResponseBean = (SdkResponse) responseBean;
      sdkResponseBean.modelHttpResponse(httpResponseHead);
    }
  }
}
