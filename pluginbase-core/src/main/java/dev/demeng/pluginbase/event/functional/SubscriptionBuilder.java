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

package dev.demeng.pluginbase.event.functional;

import dev.demeng.pluginbase.Common;
import dev.demeng.pluginbase.event.Subscription;
import dev.demeng.pluginbase.exceptions.EventHandlerException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract {@link Subscription} builder.
 *
 * @param <T> the handled / event type
 */
public interface SubscriptionBuilder<T> {

  BiConsumer<Object, Throwable> DEFAULT_EXCEPTION_CONSUMER = (object, t) -> Common.error(
      new EventHandlerException(t, object), "Error whilst handling event.", false);

  /**
   * Add a expiry predicate.
   *
   * @param predicate the expiry test
   * @return ths builder instance
   */
  @NotNull
  SubscriptionBuilder<T> expireIf(@NotNull Predicate<T> predicate);

  /**
   * Sets the expiry time on the handler
   *
   * @param duration the duration until expiry
   * @param unit     the unit for the duration
   * @return the builder instance
   * @throws IllegalArgumentException if duration is not greater than or equal to 1
   */
  @NotNull
  SubscriptionBuilder<T> expireAfter(long duration, @NotNull TimeUnit unit);

  /**
   * Sets the number of calls until the handler will automatically be unregistered
   *
   * <p>The call counter is only incremented if the event call passes all filters and if the
   * handler completes without throwing an exception.
   *
   * @param maxCalls the number of times the handler will be called until being unregistered.
   * @return the builder instance
   * @throws IllegalArgumentException if maxCalls is not greater than or equal to 1
   */
  @NotNull
  SubscriptionBuilder<T> expireAfter(long maxCalls);

  /**
   * Adds a filter to the handler.
   *
   * <p>An event will only be handled if it passes all filters. Filters are evaluated in the order
   * they are registered.
   *
   * @param predicate the filter
   * @return the builder instance
   */
  @NotNull
  SubscriptionBuilder<T> filter(@NotNull Predicate<T> predicate);

}
