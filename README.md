# aws-java-sdk-lite

A lighter option of the AWS Java SDK v2 for some services.

* S3: 12MB compressed / 31MB uncompressed to 466KB / 1.0MB

## The Problem

AWS is an important cornerstone of modern web applications. However, the "current" AWS SDK for Java ([aws/aws-sdk-java-v2](https://github.com/aws/aws-sdk-java-v2)) is large. For example, a simple application with only one dependency on the S3 client

    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>s3</artifactId>
      <version>2.21.30</version>
    </dependency>

packaged with the Maven Shade Plugin results in an über JAR with compressed size 12MB/uncompressed size 31MB. With some clever configuration -- excluding `software.amazon.awssdk:apache-client` and `software.amazon.awssdk:netty-nio-client` from the `software.amazon.awssdk:s3` dependency, adding `software.amazon.awssdk:url-connection-client:2.21.30` as a replacement client, and setting `<minimizeJar>true</minimizeJar>` on the shade plugin -- we can get the über JAR down to 5.9MB/17MB, but no smaller.

A dependency this size isn't a big deal for container-based applications. However, for some deployments, like Lambda functions that are limited to 250MB uncompressed, that's a lot of space to sacrifice just to access S3!

## Goals

Provide AWS Client libraries that:

* (Much) smaller than AWS Java SDK V2
* (Near) source compatibility with AWS Java SDK V2
* Key services and operations only

## Non-Goals

* Support all AWS services
* Support all endpoints on supported services
* Total source compatibility

## Example Code

### Get S3 Object

The library is designed to be (reasonably) source-compatible with the AWS Java SDK V2. As a result, class names, method names, and so on should be familiar at worst, and fully compatible with existing code (after organizing imports) at best. The following code retrieves an object from S3.

    try (S3Client client = S3Client.create()) {
      try (ResponseInputStream<GetObjectResponse> in=client.getObject(GetObjectRequest.builder()
          .bucket("example-bucket-name")
          .key("example/key/name.txt")
          .build())) {
        // Object exists! Handle content and response.
      }
      catch(NoSuchKeyException e) {
        // Object does not exist! Handle error.
      }
    }

In this example, credentials are pulled from the environment automatically using a process substantially similar to the official SDK.

## Modules

This library contains the following modules.

### S3

AWS S3 Client has size 466KB compressed/1.0MB and supports the following operations:

* createBucket
* headBucket
* headObject
* getObject
* deleteObject
* deleteObjects
* putObject
* listObjectsV2
* listObjectsV2Paginator
