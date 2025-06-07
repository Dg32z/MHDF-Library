package cn.chengzhiya.mhdflibrary.classpath;

import java.io.File;
import java.nio.file.Path;

public interface ClassPathAppender {
    void addJarToClasspath(Path file);

    void addJarToClasspath(File file);
}
