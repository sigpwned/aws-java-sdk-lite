package com.sigpwned.aws.sdk.lite.s3.client;

import java.util.Objects;
import com.sigpwned.aws.sdk.lite.s3.model.GetObjectResponse;

public class GetObjectResponseAndObject {
  private final GetObjectResponse response;
  private final ObjectByteSource object;

  public GetObjectResponseAndObject(GetObjectResponse response, ObjectByteSource object) {
    if (response == null)
      throw new NullPointerException();
    if (object == null)
      throw new NullPointerException();
    this.response = response;
    this.object = object;
  }

  public GetObjectResponse getResponse() {
    return response;
  }

  public ObjectByteSource getObject() {
    return object;
  }

  @Override
  public int hashCode() {
    return Objects.hash(object, response);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GetObjectResponseAndObject other = (GetObjectResponseAndObject) obj;
    return Objects.equals(object, other.object) && Objects.equals(response, other.response);
  }

  @Override
  public String toString() {
    return "GetObjectResponsePlusObject [response=" + response + ", object=" + object + "]";
  }
}
