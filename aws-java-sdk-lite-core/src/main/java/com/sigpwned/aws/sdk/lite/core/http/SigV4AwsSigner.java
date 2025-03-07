/*-
 * =================================LICENSE_START==================================
 * httpmodel-aws
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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
package com.sigpwned.aws.sdk.lite.core.http;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentials;
import com.sigpwned.aws.sdk.lite.core.auth.AwsCredentialsProvider;
import com.sigpwned.aws.sdk.lite.core.auth.credentials.AwsSessionCredentials;
import com.sigpwned.aws.sdk.lite.core.aws.AwsSigner;
import com.sigpwned.aws.sdk.lite.core.util.Hexadecimal;
import com.sigpwned.httpmodel.core.io.InputStreamBufferingStrategy;
import com.sigpwned.httpmodel.core.model.ModelHttpHeaders;
import com.sigpwned.httpmodel.core.model.ModelHttpRequest;
import com.sigpwned.httpmodel.core.util.MoreHashing;

/**
 * <p>
 * Signs the given request using the
 * <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-header-based-auth.html">AWS Sig
 * V4</a> method. Assumes no trailing headers, and single-chunk uploads.
 * </p>
 */
public class SigV4AwsSigner implements AwsSigner {
  public static final String HOST_HEADER_NAME = canonicalizeHeaderName("host");

  public static final String CONTENT_TYPE_HEADER_NAME = canonicalizeHeaderName("content-type");

  public static final String AUTHORIZATION_HEADER_NAME = canonicalizeHeaderName("authorization");

  public static final String X_AMZ_CONTENT_SHA256_HEADER_NAME =
      canonicalizeHeaderName("x-amz-content-sha256");

  public static final String X_AMZ_DATE_HEADER_NAME = canonicalizeHeaderName("x-amz-date");

  /**
   * https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html#RequestWithSTS
   */
  public static final String X_AMZ_SECURITY_TOKEN = canonicalizeHeaderName("x-amz-security-token");

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

  private static final DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");

  private static final String AWS4 = "AWS4";

  private static final String AWS4_HMAC_SHA256 = "AWS4-HMAC-SHA256";

  private static final String AWS4_REQUEST = "aws4_request";

  private final AwsCredentialsProvider credentialsProvider;
  private String accessKeyId;
  private String secretAccessKey;
  private String sessionToken;
  private String region;
  private String service;
  private String dateString;
  private String timestampString;

  public SigV4AwsSigner(AwsCredentialsProvider credentialsProvider) {
    if (credentialsProvider == null)
      throw new NullPointerException();
    this.credentialsProvider = credentialsProvider;
  }

  @Override
  public ModelHttpRequest sign(ModelHttpRequest request, String region, String service,
      OffsetDateTime now) {
    return computeAuthorizationHeaderValue(request, region, service, now);
  }

  /* default */ ModelHttpRequest computeAuthorizationHeaderValue(ModelHttpRequest request,
      String region, String service, OffsetDateTime now) {
    AwsCredentials credentials = getCredentialsProvider().resolveCredentials();
    this.accessKeyId = credentials.accessKeyId();
    this.secretAccessKey = credentials.secretAccessKey();
    this.sessionToken = credentials instanceof AwsSessionCredentials
        ? ((AwsSessionCredentials) credentials).sessionToken()
        : null;
    this.region = region;
    this.service = service;
    this.dateString = now.format(DATE_FORMAT);
    this.timestampString = now.format(TIMESTAMP_FORMAT);

    final String httpMethod = request.getMethod().toUpperCase();
    final String canonicalUri = request.getUrl().getPath();

    final String hashedPayload;
    try {
      if (!request.isBuffered())
        request.buffer(InputStreamBufferingStrategy.DEFAULT);
      hashedPayload = Hexadecimal.toHexString(MoreHashing.sha256(request));
      request.restart();
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to compute hash for request entity", e);
    }

    final String hostname = request.getHeaders().findFirstHeaderByName(HOST_HEADER_NAME)
        .map(ModelHttpHeaders.Header::getValue)
        .orElseGet(() -> request.getUrl().getAuthority().getHost().toString());

    final String contentType = request.getHeaders().findFirstHeaderByName(CONTENT_TYPE_HEADER_NAME)
        .map(ModelHttpHeaders.Header::getValue).orElse(null);

    final Map<String, String> rawHeaders = new HashMap<>();
    rawHeaders.put(HOST_HEADER_NAME, hostname);
    if (contentType != null)
      rawHeaders.put(CONTENT_TYPE_HEADER_NAME, contentType);
    request.getHeaders().stream().filter(h -> h.getName().startsWith("x-amz-")).forEach(h -> {
      rawHeaders.put(h.getName(), h.getValue());
    });
    if (!rawHeaders.containsKey(X_AMZ_CONTENT_SHA256_HEADER_NAME)) {
      rawHeaders.put(X_AMZ_CONTENT_SHA256_HEADER_NAME, hashedPayload);
      request.getHeaders().setOnlyHeader(X_AMZ_CONTENT_SHA256_HEADER_NAME, hashedPayload);
    }
    if (!rawHeaders.containsKey(X_AMZ_DATE_HEADER_NAME)) {
      rawHeaders.put(X_AMZ_DATE_HEADER_NAME, timestampString);
      request.getHeaders().setOnlyHeader(X_AMZ_DATE_HEADER_NAME, timestampString);
    }
    if (!rawHeaders.containsKey(X_AMZ_SECURITY_TOKEN) && sessionToken != null) {
      rawHeaders.put(X_AMZ_SECURITY_TOKEN, sessionToken);
      request.getHeaders().setOnlyHeader(X_AMZ_SECURITY_TOKEN, sessionToken);
    }

    final Map<String, String> canonicalHeaders =
        rawHeaders.entrySet().stream().map(SigV4AwsSigner::canonicalizeHeader)
            .sorted(HEADER_COMPARATOR).collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                SigV4AwsSigner::failMergeFunction, LinkedHashMap::new));
    final String canonicalHeaderString = canonicalHeaders.entrySet().stream()
        .map(h -> h.getKey() + ":" + h.getValue() + "\n").collect(joining(""));
    final String signedHeaders = canonicalHeaders.keySet().stream().collect(joining(";"));

    final String canonicalParameterString = extractCanonicalParameterString(request);

    final String canonicalRequestString = String.join("\n", httpMethod, canonicalUri,
        canonicalParameterString, canonicalHeaderString, signedHeaders, hashedPayload);

    final String canonicalRequestStringHash =
        sha256(canonicalRequestString.getBytes(StandardCharsets.US_ASCII));

    final String scope = String.join("/", dateString, region, service, AWS4_REQUEST);

    final String stringToSign =
        String.join("\n", AWS4_HMAC_SHA256, timestampString, scope, canonicalRequestStringHash);

    final byte[] signingKey = extractSigningKey(request);

    String signature = Hexadecimal.toHexString(hmacSha256(stringToSign, signingKey));

    // AWS4-HMAC-SHA256
    // Credential=AKIAIOSFODNN7EXAMPLE/20130524/us-east-1/s3/aws4_request,SignedHeaders=host;range;x-amz-content-sha256;x-amz-date,Signature=f0e8bdb87c964420e857bd35b5d6ed310bd44f0170aba48dd91039c6036bdb41
    String authorization =
        new StringBuilder().append(AWS4_HMAC_SHA256).append(" ").append("Credential=")
            .append(accessKeyId).append("/").append(scope).append(",").append("SignedHeaders=")
            .append(signedHeaders).append(",").append("Signature=").append(signature).toString();

    request.getHeaders().setOnlyHeader(AUTHORIZATION_HEADER_NAME, authorization);

    return request;
  }

  private AwsCredentialsProvider getCredentialsProvider() {
    return credentialsProvider;
  }

  // PARAMETERS ///////////////////////////////////////////////////////////////
  private String extractCanonicalParameterString(ModelHttpRequest request) {
    return Optional.ofNullable(request.getUrl().getQueryString())
        .map(q -> q.stream()
            .map(h -> new SimpleEntry<String, String>(h.getName(), h.getValue().orElse("")))
            .map(SigV4AwsSigner::canonicalizeParameter).sorted(PARAMETER_COMPARATOR)
            .map(p -> p.getKey() + "=" + p.getValue()).collect(joining("&")))
        .orElse("");
  }

  private static final Comparator<Map.Entry<String, String>> PARAMETER_COMPARATOR =
      Comparator.comparing(p -> p.getKey());

  private static Map.Entry<String, String> canonicalizeParameter(
      Map.Entry<String, String> rawParameter) {
    return new SimpleEntry<>(uriencode(rawParameter.getKey()), uriencode(rawParameter.getValue()));
  }

  // HEADERS //////////////////////////////////////////////////////////////////
  private static final Comparator<Map.Entry<String, String>> HEADER_COMPARATOR =
      Comparator.comparing(p -> p.getKey());

  private static String canonicalizeHeaderName(String headerName) {
    return headerName.toLowerCase();
  }

  private static String canonicalizeHeaderValue(String headerValue) {
    return headerValue.trim();
  }

  private static Map.Entry<String, String> canonicalizeHeader(Map.Entry<String, String> rawHeader) {
    return new SimpleEntry<>(canonicalizeHeaderName(rawHeader.getKey()),
        canonicalizeHeaderValue(rawHeader.getValue()));
  }

  // KEYS /////////////////////////////////////////////////////////////////////
  private byte[] extractSigningKey(ModelHttpRequest request) {
    final byte[] firstKey = (AWS4 + secretAccessKey).getBytes(StandardCharsets.US_ASCII);
    final byte[] dateKey = hmacSha256(dateString, firstKey);
    final byte[] dateRegionKey = hmacSha256(region, dateKey);
    final byte[] dateRegionServiceKey = hmacSha256(service, dateRegionKey);
    final byte[] signingKey = hmacSha256(AWS4_REQUEST, dateRegionServiceKey);
    return signingKey;
  }

  // UTILITY //////////////////////////////////////////////////////////////////

  private static <U> U failMergeFunction(U a, U b) {
    throw new AssertionError("multiple values for key");
  }

  private static String uriencode(String s) {
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (UnsupportedEncodingException e) {
      // The JVM must support utf-8, per the docs.
      throw new AssertionError("utf-8 not supported", e);
    }
  }

  private static String sha256(byte[] data) {
    return sha256(data, 0, data.length);
  }

  private static String sha256(byte[] data, int off, int len) {
    try {
      return sha256(new ByteArrayInputStream(data, off, len));
    } catch (IOException e) {
      // This should never happen, since all in memory
      throw new UncheckedIOException("Failed to hash in-memory data", e);
    }
  }

  private static String sha256(InputStream in) throws IOException {
    return Hexadecimal.toHexString(MoreHashing.sha256(in));
  }

  /**
   * JVM must support, per the spec
   *
   * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Mac
   */
  private static final String HMACSHA256 = "HmacSHA256";

  private static byte[] hmacSha256(String data, byte[] key) {
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, HMACSHA256);

    Mac mac;
    try {
      mac = Mac.getInstance(HMACSHA256);
    } catch (NoSuchAlgorithmException e) {
      // JVM is required to support SHA-256 by standard
      // https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
      throw new AssertionError("JVM does not support " + HMACSHA256, e);
    }

    try {
      mac.init(secretKeySpec);
    } catch (InvalidKeyException e) {
      // No, this is a perfectly valid key, thanks.
      throw new AssertionError("JVM does not support valid key", e);
    }

    return mac.doFinal(data.getBytes());
  }

}
