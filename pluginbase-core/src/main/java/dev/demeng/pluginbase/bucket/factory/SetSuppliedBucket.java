/*
 * MIT License
 *
 * Copyright (c) 2024 Demeng Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.demeng.pluginbase.bucket.factory;

import dev.demeng.pluginbase.bucket.AbstractBucket;
import dev.demeng.pluginbase.bucket.partitioning.PartitioningStrategy;
import java.util.Set;
import java.util.function.Supplier;

class SetSuppliedBucket<E> extends AbstractBucket<E> {

  private final Supplier<Set<E>> setSupplier;

  SetSuppliedBucket(final int size, final PartitioningStrategy<E> strategy, final Supplier<Set<E>> setSupplier) {
    super(size, strategy);
    this.setSupplier = setSupplier;
  }

  @Override
  protected Set<E> createSet() {
    return this.setSupplier.get();
  }
}
