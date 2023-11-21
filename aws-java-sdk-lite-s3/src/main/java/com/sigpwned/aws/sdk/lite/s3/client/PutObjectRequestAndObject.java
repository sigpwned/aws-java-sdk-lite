package com.sigpwned.aws.sdk.lite.s3.client;

import java.util.Objects;
import com.sigpwned.aws.sdk.lite.core.io.RequestBody;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectRequest;

public class PutObjectRequestAndObject {
  private final PutObjectRequest request;
  private final RequestBody object;

  public PutObjectRequestAndObject(PutObjectRequest request, RequestBody object) {
    if (request == null)
      throw new NullPointerException();
    if (object == null)
      throw new NullPointerException();
    this.request = request;
    this.object = object;
  }

  public PutObjectRequest getRequest() {
    return request;
  }

  public RequestBody getObject() {
    return object;
  }

  @Override
  public int hashCode() {
    return Objects.hash(object, request);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PutObjectRequestAndObject other = (PutObjectRequestAndObject) obj;
    return Objects.equals(object, other.object) && Objects.equals(request, other.request);
  }

  @Override
  public String toString() {
    return "PutObjectRequestAndObject [request=" + request + ", object=" + object + "]";
  }
}
