package cn.chengzhiya.mhdflibrary.classpath;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class ReflectionClassPathAppender implements ClassPathAppender {
    private final URLClassLoaderAccess classLoaderAccess;

    public ReflectionClassPathAppender(ClassLoader classLoader) throws IllegalStateException {
        if (classLoader instanceof URLClassLoader) {
            this.classLoaderAccess = URLClassLoaderAccess.create((URLClassLoader) classLoader);
        } else {
            throw new RuntimeException("classLoader 类型并不是 URLClassLoader");
        }
    }

    /**
     * 加载指定jar文件到classpath中
     *
     * @param file jar文件路径
     */
    @Override
    @SneakyThrows
    public void addJarToClasspath(Path file) {
        this.classLoaderAccess.addURL(file.toUri().toURL());
    }

    /**
     * 加载指定jar文件到classpath中
     *
     * @param file jar文件实例
     */
    @Override
    public void addJarToClasspath(File file) {
        addJarToClasspath(file.toPath());
    }
}
