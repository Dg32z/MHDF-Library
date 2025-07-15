package cn.chengzhiya.mhdflibrary.classpath;

import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

public abstract class URLClassLoaderAccess {
    private final URLClassLoader classLoader;

    private URLClassLoaderAccess(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static URLClassLoaderAccess create(MHDFLibrary instance, URLClassLoader classLoader) {
        Reflection reflection = new Reflection(instance, classLoader);
        if (reflection.isSupported()) {
            return reflection;
        } else if (Unsafe.isSupported()) {
            return new Unsafe(classLoader);
        } else {
            throw new RuntimeException("不支持该操作");
        }
    }

    public abstract void addURL(URL url);

    private static class Reflection extends URLClassLoaderAccess {
        private Method method;

        private Reflection(MHDFLibrary instance, URLClassLoader classLoader) {
            super(classLoader);
            try {
                this.method = instance.getReflectionManager().getMethod(URLClassLoader.class, "addURL", true, URL.class);
            } catch (Exception e) {
                this.method = null;
            }
        }

        private boolean isSupported() {
            return this.method != null;
        }

        @Override
        @SneakyThrows
        public void addURL(URL url) {
            this.method.invoke(super.classLoader, url);
        }
    }

    private static class Unsafe extends URLClassLoaderAccess {
        private static final sun.misc.Unsafe UNSAFE;

        static {
            sun.misc.Unsafe unsafe;
            try {
                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (sun.misc.Unsafe) unsafeField.get(null);
            } catch (Throwable t) {
                unsafe = null;
            }
            UNSAFE = unsafe;
        }

        private final Collection<URL> unopenedURLs;
        private final Collection<URL> pathURLs;

        @SuppressWarnings("unchecked")
        Unsafe(URLClassLoader classLoader) {
            super(classLoader);

            Collection<URL> unopenedURLs;
            Collection<URL> pathURLs;
            try {
                Object ucp = fetchField(URLClassLoader.class, classLoader, "ucp");
                unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
                pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");
            } catch (Throwable e) {
                unopenedURLs = null;
                pathURLs = null;
            }

            this.unopenedURLs = unopenedURLs;
            this.pathURLs = pathURLs;
        }

        private static boolean isSupported() {
            return UNSAFE != null;
        }

        private static Object fetchField(final Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
            Field field = clazz.getDeclaredField(name);
            long offset = UNSAFE.objectFieldOffset(field);
            return UNSAFE.getObject(object, offset);
        }

        @Override
        public void addURL(URL url) {
            if (this.unopenedURLs == null || this.pathURLs == null) {
                throw new RuntimeException("载入的地址不存在");
            }

            synchronized (this.unopenedURLs) {
                this.unopenedURLs.add(url);
                this.pathURLs.add(url);
            }
        }
    }
}
