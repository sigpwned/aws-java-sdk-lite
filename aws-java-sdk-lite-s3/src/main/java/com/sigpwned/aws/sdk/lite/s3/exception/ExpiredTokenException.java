/*-
 * =================================LICENSE_START==================================
 * aws-java-sdk-lite-s3
 * ====================================SECTION=====================================
 * Copyright (C) 2023 Andy Boothe
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
package com.sigpwned.aws.sdk.lite.s3.exception;

import com.sigpwned.aws.sdk.lite.s3.S3Exception;

/**
 * <p>
 * Credentials have expired.
 * </p>
 *
 * <pre>
 *   &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 *     &lt;Error&gt;
 *     &lt;Code&gt;ExpiredToken&lt;/Code&gt;
 *     &lt;Message&gt;The provided token has expired.&lt;/Message&gt;
 *     &lt;Token-0&gt;IQoJb3JpZ2luX2VjEE4aCXVzLWVhc3QtMiJGMEQCIAwQxVkNNiMfgcnvxWY54Gc+p2ii2fLjT+8bxmcsYLOvAiAG8BalD7MmxX4hPshT94yLIkt7LUA/kGbpaUQxKnzxRSqOAwiY//////////8BEAIaDDk0MzkwODg2ODY2NSIMhX+/lFPwpKV2k+Q7KuICaWj3BXPldbkh/5Phh3CY+2xtcYoyX5PGpI387OIP2LRNJed0oFMsGxnp4Cgw5rgB54xq7zPlrJxpP4yTHfPRArkP7k1D0gQ3C2tDYKFZWkh72oBBFgRLneGhftwaalggbya7gOM8CLCGsol5fG9AUOl1xit5Gp1K9RQKgy9NCtTmaBXiwqit7Lg4GrQo/zG7BqlTcZS9C8Fo/L0elwIVqt+XY8evhNtNR22rsQe0iWFVlM+7NG0e3t6XJzJFjZ7AeH4KVyjhxFm42laj6/J32PXtbklkgpiT1IgmQB2pPe0grGk1ntF9fbOfikNGlACYz2/POYY8tGPtZhXkCiMc/yBR28/oOovCR/eJobXZiTLBDjQm4QpKQkaIu5tZFwuTdYYKlmK5XigJNAyMgzysb6biw1rTKF5va7e0mOhlG//I8FfViAlzbA5eyzY+tkvmJZTERatgRG0F9VL/BTXEMMy7MLys2qoGOqcBtFDgbqDZdT5M+WO+MiVo6kMeF5DdAC/7m+WK+vn9IfUWNMHxy0XpNkT/kXQzPNoL0lWyoOSnJMTlfwesjn62kse1wCPHMY1MTBTSgXnhDWNX4Gotiqe0OCuGz+PBMyrX2gS844zQOIQt0trHWQI2MU24lL14NCfmuwRQIMZKctGq/KvMRRI7FJK9nfbvyTwXEPckIz77fJVdHLwvGyoTRC7pkDZGhbs=&lt;/Token-0&gt;
 *     &lt;RequestId&gt;N9J40A784A6JQ9PJ&lt;/RequestId&gt;
 *     &lt;HostId&gt;OR1O/wW8LDfyQa4pBQTxKm0JmfKoxf9iituPvSii+vFdxSt/7uemxtKSiFYIR12IUR0gBHH1Kw4=&lt;/HostId&gt;
 *   &lt;/Error&gt;
 * </pre>
 */
public class ExpiredTokenException extends S3Exception {

  // <?xml version="1.0" encoding="UTF-8"?>
  // <Error>
  // <Code>ExpiredToken</Code>
  // <Message>The provided token has expired.</Message>
  // <Token-0>IQoJb3JpZ2luX2VjEE4aCXVzLWVhc3QtMiJGMEQCIAwQxVkNNiMfgcnvxWY54Gc+p2ii2fLjT+8bxmcsYLOvAiAG8BalD7MmxX4hPshT94yLIkt7LUA/kGbpaUQxKnzxRSqOAwiY//////////8BEAIaDDk0MzkwODg2ODY2NSIMhX+/lFPwpKV2k+Q7KuICaWj3BXPldbkh/5Phh3CY+2xtcYoyX5PGpI387OIP2LRNJed0oFMsGxnp4Cgw5rgB54xq7zPlrJxpP4yTHfPRArkP7k1D0gQ3C2tDYKFZWkh72oBBFgRLneGhftwaalggbya7gOM8CLCGsol5fG9AUOl1xit5Gp1K9RQKgy9NCtTmaBXiwqit7Lg4GrQo/zG7BqlTcZS9C8Fo/L0elwIVqt+XY8evhNtNR22rsQe0iWFVlM+7NG0e3t6XJzJFjZ7AeH4KVyjhxFm42laj6/J32PXtbklkgpiT1IgmQB2pPe0grGk1ntF9fbOfikNGlACYz2/POYY8tGPtZhXkCiMc/yBR28/oOovCR/eJobXZiTLBDjQm4QpKQkaIu5tZFwuTdYYKlmK5XigJNAyMgzysb6biw1rTKF5va7e0mOhlG//I8FfViAlzbA5eyzY+tkvmJZTERatgRG0F9VL/BTXEMMy7MLys2qoGOqcBtFDgbqDZdT5M+WO+MiVo6kMeF5DdAC/7m+WK+vn9IfUWNMHxy0XpNkT/kXQzPNoL0lWyoOSnJMTlfwesjn62kse1wCPHMY1MTBTSgXnhDWNX4Gotiqe0OCuGz+PBMyrX2gS844zQOIQt0trHWQI2MU24lL14NCfmuwRQIMZKctGq/KvMRRI7FJK9nfbvyTwXEPckIz77fJVdHLwvGyoTRC7pkDZGhbs=</Token-0>
  // <RequestId>N9J40A784A6JQ9PJ</RequestId>
  // <HostId>OR1O/wW8LDfyQa4pBQTxKm0JmfKoxf9iituPvSii+vFdxSt/7uemxtKSiFYIR12IUR0gBHH1Kw4=</HostId>
  // </Error>


  private static final long serialVersionUID = -4509974125071373332L;

  public ExpiredTokenException() {
    super("expired token");
  }
}
