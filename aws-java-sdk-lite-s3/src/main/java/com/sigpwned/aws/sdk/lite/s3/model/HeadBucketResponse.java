package com.sigpwned.aws.sdk.lite.s3.model;

import com.sigpwned.aws.sdk.lite.core.SdkResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(fluent = true, chain = true)
public class HeadBucketResponse extends SdkResponse {
}
