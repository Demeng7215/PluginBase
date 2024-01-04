/*
 * MIT License
 *
 * Copyright (c) 2024 Demeng Chen
 * Copyright (c) lucko (Luck) <luck@lucko.me>
 * Copyright (c) lucko/helper contributors
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

package dev.demeng.pluginbase.dependencyloader.dependency;

import dev.demeng.pluginbase.exceptions.BaseException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to {@link URLClassLoader}#addURL.
 */
public abstract class URLClassLoaderAccess {

  /**
   * Creates a {@link URLClassLoaderAccess} for the given class loader.
   *
   * @param classLoader the class loader
   * @return the access object
   */
  static URLClassLoaderAccess create(final URLClassLoader classLoader) {
    if (Reflection.isSupported()) {
      return new Reflection(classLoader);
    } else if (Unsafe.isSupported()) {
      return new Unsafe(classLoader);
    } else {
      return Noop.INSTANCE;
    }
  }

  private final URLClassLoader classLoader;

  protected URLClassLoaderAccess(final URLClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  /**
   * Adds the given URL to the class loader.
   *
   * @param url the URL to add
   */
  public abstract void addURL(@NotNull URL url);

  /**
   * Accesses using reflection, not supported on Java 9+.
   */
  private static class Reflection extends URLClassLoaderAccess {

    private static final Method ADD_URL_METHOD;

    static {
      Method addUrlMethod;

      try {
        addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addUrlMethod.setAccessible(true);

      } catch (final Exception ex) {
        addUrlMethod = null;
      }

      ADD_URL_METHOD = addUrlMethod;
    }

    private static boolean isSupported() {
      return ADD_URL_METHOD != null;
    }

    Reflection(final URLClassLoader classLoader) {
      super(classLoader);
    }

    @Override
    public void addURL(@NotNull final URL url) {
      try {
        ADD_URL_METHOD.invoke(super.classLoader, url);
      } catch (final ReflectiveOperationException ex) {
        throw new BaseException(ex);
      }
    }
  }

  /**
   * Accesses using sun.misc.Unsafe, supported on Java 9+.
   *
   * @author Vaishnav Anil (https://github.com/slimjar/slimjar)
   */
  private static class Unsafe extends URLClassLoaderAccess {

    private static final sun.misc.Unsafe UNSAFE;

    static {
      sun.misc.Unsafe unsafe;

      try {
        final Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        unsafe = (sun.misc.Unsafe) unsafeField.get(null);

      } catch (final Throwable t) {
        unsafe = null;
      }
      UNSAFE = unsafe;
    }

    private static boolean isSupported() {
      return UNSAFE != null;
    }

    private final Collection<URL> unopenedURLs;
    private final Collection<URL> pathURLs;

    @SuppressWarnings("unchecked")
    Unsafe(final URLClassLoader classLoader) {
      super(classLoader);

      Collection<URL> unopenedURLs;
      Collection<URL> pathURLs;

      try {
        final Object ucp = fetchField(URLClassLoader.class, classLoader, "ucp");
        unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
        pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");

      } catch (final Throwable t) {
        unopenedURLs = null;
        pathURLs = null;
      }

      this.unopenedURLs = unopenedURLs;
      this.pathURLs = pathURLs;
    }

    private static Object fetchField(final Class<?> clazz, final Object object, final String name)
        throws NoSuchFieldException {
      final Field field = clazz.getDeclaredField(name);
      final long offset = UNSAFE.objectFieldOffset(field);
      return UNSAFE.getObject(object, offset);
    }

    @Override
    public void addURL(@NotNull final URL url) {
      this.unopenedURLs.add(url);
      this.pathURLs.add(url);
    }
  }

  private static class Noop extends URLClassLoaderAccess {

    private static final Noop INSTANCE = new Noop();

    private Noop() {
      super(null);
    }

    @Override
    public void addURL(@NotNull final URL url) {
      throw new UnsupportedOperationException();
    }
  }
}
