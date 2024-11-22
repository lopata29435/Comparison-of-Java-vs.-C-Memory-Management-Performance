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
package com.ionutbalosin.jvm.performance.benchmarks.miscellaneous.huffmancoding;

public class HuffmanNode {
  protected char ch;
  protected int freq;
  protected HuffmanNode left = null, right = null;

  HuffmanNode(char ch, int freq) {
    this.ch = ch;
    this.freq = freq;
  }

  public HuffmanNode(char ch, int freq, HuffmanNode left, HuffmanNode right) {
    this.ch = ch;
    this.freq = freq;
    this.left = left;
    this.right = right;
  }

  public static boolean isLeaf(HuffmanNode root) {
    return root.left == null && root.right == null;
  }
}
