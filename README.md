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

## Results

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
