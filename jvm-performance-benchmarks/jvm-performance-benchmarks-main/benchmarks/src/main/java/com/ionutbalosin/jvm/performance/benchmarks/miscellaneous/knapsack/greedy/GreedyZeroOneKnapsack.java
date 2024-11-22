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
package com.ionutbalosin.jvm.performance.benchmarks.miscellaneous.knapsack.greedy;

import com.ionutbalosin.jvm.performance.benchmarks.miscellaneous.knapsack.Item;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyZeroOneKnapsack {

  public static List<Item> knapsack(int capacity, List<Item> items) {
    items.sort(
        Comparator.comparingDouble(item -> (double) ((Item) item).value / ((Item) item).weight)
            .reversed());

    final List<Item> selectedItems = new ArrayList<>();

    int currentWeight = 0;
    for (Item item : items) {
      if (currentWeight + item.weight <= capacity) {
        selectedItems.add(item);
        currentWeight += item.weight;
      }
    }

    return selectedItems;
  }
}
