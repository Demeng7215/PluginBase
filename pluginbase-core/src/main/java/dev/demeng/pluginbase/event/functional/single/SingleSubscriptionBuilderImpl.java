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

package dev.demeng.pluginbase.event.functional.single;

import dev.demeng.pluginbase.event.SingleSubscription;
import dev.demeng.pluginbase.event.functional.ExpiryTestStage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

class SingleSubscriptionBuilderImpl<T extends Event> implements SingleSubscriptionBuilder<T> {

  final Class<T> eventClass;
  final EventPriority priority;

  BiConsumer<? super T, Throwable> exceptionConsumer = DEFAULT_EXCEPTION_CONSUMER;
  boolean handleSubclasses = false;

  final List<Predicate<T>> filters = new ArrayList<>(3);
  final List<BiPredicate<SingleSubscription<T>, T>> preExpiryTests = new ArrayList<>(0);
  final List<BiPredicate<SingleSubscription<T>, T>> midExpiryTests = new ArrayList<>(0);
  final List<BiPredicate<SingleSubscription<T>, T>> postExpiryTests = new ArrayList<>(0);

  SingleSubscriptionBuilderImpl(final Class<T> eventClass, final EventPriority priority) {
    this.eventClass = eventClass;
    this.priority = priority;
  }

  @NotNull
  @Override
  public SingleSubscriptionBuilder<T> expireIf(
      @NotNull final BiPredicate<SingleSubscription<T>, T> predicate,
      @NotNull final ExpiryTestStage... testPoints) {
    Objects.requireNonNull(testPoints, "testPoints");
    Objects.requireNonNull(predicate, "predicate");
    for (final ExpiryTestStage testPoint : testPoints) {
      switch (testPoint) {
        case PRE:
          this.preExpiryTests.add(predicate);
          break;
        case POST_FILTER:
          this.midExpiryTests.add(predicate);
          break;
        case POST_HANDLE:
          this.postExpiryTests.add(predicate);
          break;
        default:
          throw new IllegalArgumentException("Unknown ExpiryTestPoint: " + testPoint);
      }
    }
    return this;
  }

  @NotNull
  @Override
  public SingleSubscriptionBuilder<T> filter(@NotNull final Predicate<T> predicate) {
    Objects.requireNonNull(predicate, "predicate");
    this.filters.add(predicate);
    return this;
  }

  @NotNull
  @Override
  public SingleSubscriptionBuilder<T> exceptionConsumer(
      @NotNull final BiConsumer<? super T, Throwable> exceptionConsumer) {
    Objects.requireNonNull(exceptionConsumer, "exceptionConsumer");
    this.exceptionConsumer = exceptionConsumer;
    return this;
  }

  @NotNull
  @Override
  public SingleSubscriptionBuilder<T> handleSubclasses() {
    this.handleSubclasses = true;
    return this;
  }

  @NotNull
  @Override
  public SingleHandlerList<T> handlers() {
    return new SingleHandlerListImpl<>(this);
  }

}
