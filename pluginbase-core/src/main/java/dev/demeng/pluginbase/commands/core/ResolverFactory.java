/*
 * This file is part of lamp, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package dev.demeng.pluginbase.commands.core;

import dev.demeng.pluginbase.commands.command.CommandParameter;
import dev.demeng.pluginbase.commands.process.ContextResolverFactory;
import dev.demeng.pluginbase.commands.process.ValueResolverFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ResolverFactory {

  private final Object factory;

  public ResolverFactory(final Object factory) {
    this.factory = factory;
  }

  public @Nullable Resolver create(@NotNull final CommandParameter parameter) {
    final Object resolver;

    if (factory instanceof ContextResolverFactory) {
      resolver = ((ContextResolverFactory) factory).create(parameter);
    } else {
      resolver = ((ValueResolverFactory) factory).create(parameter);
    }

    if (resolver == null) {
      return null;
    }
    return Resolver.wrap(resolver);
  }

}