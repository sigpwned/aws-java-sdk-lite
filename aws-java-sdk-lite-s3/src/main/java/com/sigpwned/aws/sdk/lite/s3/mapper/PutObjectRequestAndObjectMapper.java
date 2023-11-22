package com.sigpwned.aws.sdk.lite.s3.mapper;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Optional;
import com.sigpwned.aws.sdk.lite.core.io.RequestBody;
import com.sigpwned.aws.sdk.lite.s3.client.PutObjectRequestAndObject;
import com.sigpwned.aws.sdk.lite.s3.model.PutObjectRequest;
import com.sigpwned.httpmodel.core.client.bean.ModelHttpBeanClientRequestMapper;
import com.sigpwned.httpmodel.core.model.ModelHttpMediaType;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.model.ModelHttpRequestHead;
import com.sigpwned.httpmodel.core.util.ModelHttpMethods;

public class PutObjectRequestAndObjectMapper
    implements ModelHttpBeanClientRequestMapper<PutObjectRequestAndObject> {
  @Override
  public boolean isMappable(Class<?> requestType, ModelHttpMediaType contentType) {
    return requestType.equals(PutObjectRequestAndObject.class);
  }

  @Override
  public ModelHttpRequest mapRequest(ModelHttpRequestHead httpRequestHead,
      PutObjectRequestAndObject requestAndObject) throws IOException {
    PutObjectRequest request = requestAndObject.getRequest();
    RequestBody object = requestAndObject.getObject();

    Long contentLength =
        Optional.ofNullable(object.getContentLength()).orElse(request.contentLength());
    String contentType = Optional.ofNullable(object.getContentType()).orElse(request.contentType());

    return new ModelHttpRequest(
        httpRequestHead.toBuilder().method(ModelHttpMethods.PUT).url()
            .appendPath(format("%s/%s", request.bucket(), request.key())).done().headers()
            .setOnlyHeader("x-amz-acl",
                Optional.ofNullable(request.acl()).map(Object::toString).orElse(null))
            .setOnlyHeader("Cache-Control",
                Optional.ofNullable(request.cacheControl()).map(Object::toString).orElse(null))
            .setOnlyHeader("Content-Disposition",
                Optional.ofNullable(request.contentDisposition()).map(Object::toString)
                    .orElse(null))
            .setOnlyHeader("Content-Encoding",
                Optional.ofNullable(request.contentEncoding()).map(Object::toString).orElse(null))
            .setOnlyHeader("Content-Language",
                Optional.ofNullable(request.contentLanguage()).map(Object::toString).orElse(null))
            .setOnlyHeader("Content-Length",
                Optional.ofNullable(contentLength).map(Object::toString).orElse(null))
            .setOnlyHeader("Content-MD5",
                Optional.ofNullable(request.contentMD5()).map(Object::toString).orElse(null))
            .setOnlyHeader("Content-Type",
                Optional.ofNullable(contentType).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-sdk-checksum-algorithm",
                Optional.ofNullable(request.checksumAlgorithm()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-checksum-crc32",
                Optional.ofNullable(request.checksumCRC32()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-checksum-crc32c",
                Optional.ofNullable(request.checksumCRC32C()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-checksum-sha1",
                Optional.ofNullable(request.checksumSHA1()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-checksum-sha256",
                Optional.ofNullable(request.checksumSHA256()).map(Object::toString).orElse(null))
            .setOnlyHeader("Expires",
                Optional.ofNullable(request.expires()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-grant-full-control",
                Optional.ofNullable(request.grantFullControl()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-grant-read",
                Optional.ofNullable(request.grantRead()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-grant-read-acp",
                Optional.ofNullable(request.grantReadACP()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-grant-write-acp",
                Optional.ofNullable(request.grantWriteACP()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption",
                Optional.ofNullable(request.serverSideEncryption()).map(Object::toString)
                    .orElse(null))
            .setOnlyHeader("x-amz-storage-class",
                Optional.ofNullable(request.storageClass()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-website-redirect-location",
                Optional.ofNullable(request.storageClass()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption-customer-algorithm",
                Optional.ofNullable(request.sseCustomerAlgorithm()).map(Object::toString)
                    .orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption-customer-key",
                Optional.ofNullable(request.sseCustomerKey()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption-customer-key-MD5",
                Optional.ofNullable(request.sseCustomerKeyMD5()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption-aws-kms-key-id",
                Optional.ofNullable(request.ssekmsKeyId()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption-context",
                Optional.ofNullable(request.ssekmsEncryptionContext()).map(Object::toString)
                    .orElse(null))
            .setOnlyHeader("x-amz-server-side-encryption-bucket-key-enabled",
                Optional.ofNullable(request.bucketKeyEnabled()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-request-payer",
                Optional.ofNullable(request.requestPayer()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-tagging",
                Optional.ofNullable(request.tagging()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-object-lock-mode",
                Optional.ofNullable(request.objectLockMode()).map(Object::toString).orElse(null))
            .setOnlyHeader("x-amz-object-lock-retain-until-date",
                Optional.ofNullable(request.objectLockRetainUntilDate()).map(Object::toString)
                    .orElse(null))
            .setOnlyHeader("x-amz-object-lock-legal-hold",
                Optional.ofNullable(request.objectLockLegalHoldStatus()).map(Object::toString)
                    .orElse(null))
            .setOnlyHeader("x-amz-expected-bucket-owner", Optional
                .ofNullable(request.expectedBucketOwner()).map(Object::toString).orElse(null))
            .done().build(),
        object.getContentStreamProvider().newStream());
  }
}
