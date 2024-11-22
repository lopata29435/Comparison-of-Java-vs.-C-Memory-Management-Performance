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
package com.ionutbalosin.jvm.performance.benchmarks.api.diskio;

import static com.ionutbalosin.jvm.performance.benchmarks.api.diskio.util.CharUtils.charArray;

import com.ionutbalosin.jvm.performance.benchmarks.api.diskio.util.FileUtil.ChunkSize;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

/*
 * Measures the time it takes to read character array chunks using a PipedReader and includes a sanity check to verify the number of characters read.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 5)
@State(Scope.Benchmark)
public class PipedReaderBenchmark {

  // $ java -jar */*/benchmarks.jar ".*PipedReaderBenchmark.*"

  private final int ISO_8859_1 = 0xFF;

  @Param private ChunkSize chunkSize;

  private PipedReader pr;
  private PipedWriter pw;
  private char[] data;
  private Thread writerThread;
  private CountDownLatch writerLatch;
  private AtomicBoolean isWriterRunning;

  @Setup(Level.Trial)
  public void beforeTrial() throws IOException {
    data = charArray(chunkSize.get(), ISO_8859_1);
  }

  @Setup(Level.Iteration)
  public void beforeIteration() throws IOException, InterruptedException {
    pr = new PipedReader(chunkSize.get());
    pw = new PipedWriter(pr);

    isWriterRunning = new AtomicBoolean(true);
    writerLatch = new CountDownLatch(1);
    writerThread =
        new Thread(
            () -> {
              writerLatch.countDown();
              while (isWriterRunning.get()) {
                try {
                  pw.write(data);
                  pw.flush();
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            });

    writerThread.start();
    // ensure that the writer thread has started before the benchmark iterations
    writerLatch.await();
  }

  @TearDown(Level.Iteration)
  public void afterIteration() throws IOException, InterruptedException {
    isWriterRunning.set(false);
    writerThread.join();
    pr.close();
    pw.close();
  }

  @Benchmark
  public char[] read() throws IOException, InterruptedException {
    final char[] buffer = new char[chunkSize.get()];
    int charsRead = pr.read(buffer, 0, chunkSize.get());

    if (charsRead == -1) {
      afterIteration();
      beforeIteration();
    } else {
      sanityCheck(charsRead, chunkSize.get());
    }

    return buffer;
  }

  private void sanityCheck(int actualBytes, int expectedBytes) {
    if (actualBytes != expectedBytes) {
      throw new AssertionError(
          "Number of bytes mismatch. Actual: " + actualBytes + ", expected: " + expectedBytes);
    }
  }
}
