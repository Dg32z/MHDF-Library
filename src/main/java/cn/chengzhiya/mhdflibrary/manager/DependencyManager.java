package cn.chengzhiya.mhdflibrary.manager;

import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import cn.chengzhiya.mhdflibrary.classpath.ClassPathAppender;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RepositoryConfig;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

@Getter
public final class DependencyManager {
    private final ClassPathAppender classPathAppender;
    private final CopyOnWriteArrayList<DependencyConfig> loadedDependencyList = new CopyOnWriteArrayList<>();

    public DependencyManager(ClassPathAppender classPathAppender) {
        this.classPathAppender = classPathAppender;
    }

    /**
     * 下载指定依赖配置实例对应的依赖
     *
     * @param dependencyConfig 依赖配置实例
     */
    @SneakyThrows
    private void downloadDependency(DependencyConfig dependencyConfig) {
        File file = new File(MHDFLibrary.instance.getLibraryFolder(), dependencyConfig.getFileName());
        if (file.exists()) {
            return;
        }

        RepositoryConfig repositoryConfig = dependencyConfig.getRepository();
        String url = repositoryConfig.getDependencyUrl(dependencyConfig);

        MHDFLibrary.instance.getLoggerManager().log("正在下载依赖 " + dependencyConfig.getFileName() + "(" + url + ")");
        MHDFLibrary.instance.getHttpManager().downloadFile(url, file.toPath());
    }

    /**
     * 下载指定依赖配置实例列表对应的全部依赖
     *
     * @param dependencies 依赖配置实例列表
     */
    public void downloadDependencies(Collection<DependencyConfig> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (DependencyConfig dependencyConfig : dependencies) {
            new Thread(() -> {
                if (!dependencyConfig.isEnable()) {
                    latch.countDown();
                    return;
                }

                if (getLoadedDependencyList().contains(dependencyConfig)) {
                    latch.countDown();
                    return;
                }

                try {
                    downloadDependency(dependencyConfig);
                } catch (Throwable e) {
                    throw new RuntimeException("无法下载依赖 " + dependencyConfig.getFileName(), e);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 加载指定依赖配置实例对应的依赖
     *
     * @param dependencyConfig 依赖配置实例
     */
    private void loadDependency(DependencyConfig dependencyConfig) {
        if (getLoadedDependencyList().contains(dependencyConfig)) {
            return;
        }

        MHDFLibrary.instance.getLoggerManager().log("正在加载依赖 " + dependencyConfig.getFileName());
        this.classPathAppender.addJarToClasspath(
                MHDFLibrary.instance.getRelocatorManager().relocation(dependencyConfig)
        );
        getLoadedDependencyList().add(dependencyConfig);
        MHDFLibrary.instance.getLoggerManager().log("依赖 " + dependencyConfig.getFileName() + " 加载完成!");
    }


    /**
     * 加载指定依赖配置实例列表对应的全部依赖
     *
     * @param dependencies 依赖配置实例列表
     */
    public void loadDependencies(Collection<DependencyConfig> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (DependencyConfig dependencyConfig : dependencies) {
            new Thread(() -> {
                if (!dependencyConfig.isEnable()) {
                    latch.countDown();
                    return;
                }

                if (getLoadedDependencyList().contains(dependencyConfig)) {
                    latch.countDown();
                    return;
                }

                try {
                    loadDependency(dependencyConfig);
                } catch (Throwable e) {
                    throw new RuntimeException("无法下载依赖 " + dependencyConfig.getFileName(), e);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
