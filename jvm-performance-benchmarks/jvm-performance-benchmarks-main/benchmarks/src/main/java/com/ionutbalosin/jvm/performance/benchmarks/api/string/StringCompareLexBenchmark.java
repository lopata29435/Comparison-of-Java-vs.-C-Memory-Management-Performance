/*
 * JVM Performance Benchmarks
 *
 * Copyright (C) 2019 - 2024 Ionut Balosin
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ionutbalosin.jvm.performance.benchmarks.api.string;

import static com.ionutbalosin.jvm.performance.benchmarks.api.string.utils.StringUtils.encodeCharArray;
import static com.ionutbalosin.jvm.performance.benchmarks.api.string.utils.StringUtils.generateCommonEnglishCharArray;

import com.ionutbalosin.jvm.performance.benchmarks.api.string.utils.StringUtils.ComparisonType;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/*
 * This benchmark evaluates the performance of various string lexicographical comparison operations,
 * such as 'compareTo' and 'compareToIgnoreCase'.
 *
 * The generated strings are encoding-specific, representing distinct character sets—specifically
 * Latin-1 and UTF-16. Additionally, variations between these encodings (Latin-1 to UTF-16) are
 * included to cover a wider range of scenarios.
 *
 * All string comparisons assume inspection of the entire character arrays to determine the
 * result. This deliberate design aims to trigger an exhaustive number of checks, involving a higher
 * number of (char/byte arrays) comparisons to determine the result.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 5)
@State(Scope.Benchmark)
public class StringCompareLexBenchmark {

  // $ java -jar */*/benchmarks.jar ".*StringCompareLexBenchmark.*"

  private String sourceStr, uppercaseSourceStr;
  private String targetStr, lowercaseTargetStr;

  @Param private ComparisonType comparisonType;

  @Param({"1024"})
  private int length;

  @Setup
  public void setup() {
    // Generate encoding-specific sources
    final char[] sourceChArray =
        generateCommonEnglishCharArray(length, comparisonType.getSourceCoder());
    sourceStr = new String(sourceChArray);
    uppercaseSourceStr = sourceStr.toUpperCase();

    // Generate encoding-specific targets derived from the same source character array
    // Note: This creates equivalent Strings to the source, possibly with different encodings
    // (according to the target coder), except when converting from UTF-16 to Latin-1, which may
    // result in unequal strings due to character loss.
    final byte[] targetByteArray = encodeCharArray(sourceChArray, comparisonType.getTargetCoder());
    targetStr = new String(targetByteArray, comparisonType.getTargetCoder().getCharset());
    lowercaseTargetStr = targetStr.toLowerCase();
  }

  @Benchmark
  public int compare_to() {
    return sourceStr.compareTo(targetStr);
  }

  @Benchmark
  public int compare_to_ignore_case() {
    return uppercaseSourceStr.compareToIgnoreCase(lowercaseTargetStr);
  }
}
