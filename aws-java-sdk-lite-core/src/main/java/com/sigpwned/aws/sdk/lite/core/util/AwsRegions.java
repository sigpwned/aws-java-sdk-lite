package com.sigpwned.aws.sdk.lite.core.util;

import com.sigpwned.aws.sdk.lite.core.util.Env.OptionalEnvironmentVariable;

/**
 * https://github.com/aws/aws-sdk-java-v2/blob/c121b31dbc51b23260dd0fa366c4ad3563cbb2fe/codegen-lite/src/test/resources/software/amazon/awssdk/codegen/lite/regions/regions.java
 */
public final class AwsRegions {
  private AwsRegions() {}

  public static final String GOV_CLOUD = "us-gov-west-1";
  public static final String US_GOV_EAST_1 = "us-gov-east-1";
  public static final String US_EAST_1 = "us-east-1";
  public static final String US_EAST_2 = "us-east-2";
  public static final String US_WEST_1 = "us-west-1";
  public static final String US_WEST_2 = "us-west-2";
  public static final String EU_WEST_1 = "eu-west-1";
  public static final String EU_WEST_2 = "eu-west-2";
  public static final String EU_WEST_3 = "eu-west-3";
  public static final String EU_CENTRAL_1 = "eu-central-1";
  public static final String EU_CENTRAL_2 = "eu-central-2";
  public static final String EU_NORTH_1 = "eu-north-1";
  public static final String EU_SOUTH_1 = "eu-south-1";
  public static final String EU_SOUTH_2 = "eu-south-2";
  public static final String AP_EAST_1 = "ap-east-1";
  public static final String AP_SOUTH_1 = "ap-south-1";
  public static final String AP_SOUTH_2 = "ap-south-2";
  public static final String AP_SOUTHEAST_1 = "ap-southeast-1";
  public static final String AP_SOUTHEAST_2 = "ap-southeast-2";
  public static final String AP_SOUTHEAST_3 = "ap-southeast-3";
  public static final String AP_SOUTHEAST_4 = "ap-southeast-4";
  public static final String AP_NORTHEAST_1 = "ap-northeast-1";
  public static final String AP_NORTHEAST_2 = "ap-northeast-2";
  public static final String AP_NORTHEAST_3 = "ap-northeast-3";
  public static final String SA_EAST_1 = "sa-east-1";
  public static final String CN_NORTH_1 = "cn-north-1";
  public static final String CN_NORTHWEST_1 = "cn-northwest-1";
  public static final String CA_CENTRAL_1 = "ca-central-1";
  public static final String ME_CENTRAL_1 = "me-central-1";
  public static final String ME_SOUTH_1 = "me-south-1";
  public static final String AF_SOUTH_1 = "af-south-1";
  public static final String US_ISO_EAST_1 = "us-iso-east-1";
  public static final String US_ISOB_EAST_1 = "us-isob-east-1";
  public static final String US_ISO_WEST_1 = "us-iso-west-1";
  public static final String IL_CENTRAL_1 = "il-central-1";

  /**
   * https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html#envvars-list-AWS_REGION
   */
  public static final String AWS_REGION_NAME = "AWS_REGION";

  /**
   * https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html#envvars-list-AWS_DEFAULT_REGION
   */
  public static final String AWS_DEFAULT_REGION_NAME = "AWS_DEFAULT_REGION";

  public static OptionalEnvironmentVariable<String> findDefaultRegion() {
    return Env.getenv(AWS_REGION_NAME).or(() -> Env.getenv(AWS_DEFAULT_REGION_NAME));
  }
}
