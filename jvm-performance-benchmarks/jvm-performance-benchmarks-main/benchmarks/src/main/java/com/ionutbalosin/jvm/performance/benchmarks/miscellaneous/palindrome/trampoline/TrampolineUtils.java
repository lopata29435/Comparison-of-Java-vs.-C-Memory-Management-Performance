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
package com.ionutbalosin.jvm.performance.benchmarks.miscellaneous.palindrome.trampoline;

public class TrampolineUtils {

  public static <T> ATrampoline<T> next(final ATrampoline<T> nextCall) {
    return nextCall;
  }

  public static <T> ATrampoline<T> done(final T value) {
    return new ATrampoline<T>() {
      @Override
      public boolean isComplete() {
        return true;
      }

      @Override
      public T result() {
        return value;
      }

      @Override
      public ATrampoline<T> jump() {
        throw new UnsupportedOperationException();
      }
    };
  }
}