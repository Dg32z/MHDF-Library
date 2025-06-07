package cn.chengzhiya.mhdflibrary;

import cn.chengzhiya.mhdflibrary.classpath.ReflectionClassPathAppender;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RepositoryConfig;
import cn.chengzhiya.mhdflibrary.manager.*;
import lombok.Getter;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public final class MHDFLibrary {
    public static RepositoryConfig mavenCenterMirror = new RepositoryConfig("https://repo.huaweicloud.com/repository/maven/", "huawei-maven");

    public static MHDFLibrary instance;

    private final HttpManager httpManager;
    private final ReflectionManager reflectionManager;
    private final DependencyManager dependencyManager;
    private final RelocatorManager relocatorManager;
    private final LoggerManager loggerManager;

    private final String relocatorPrefix;
    private final File libraryFolder;

    private final List<DependencyConfig> defaultDependencyList = List.of(
            new DependencyConfig(
                    "org.ow2.asm",
                    "asm",
                    "9.7.1",
                    mavenCenterMirror
            ),
            new DependencyConfig(
                    "org.ow2.asm",
                    "asm-commons",
                    "9.7.1",
                    mavenCenterMirror
            ),
            new DependencyConfig(
                    "me.lucko",
                    "jar-relocator",
                    "1.7",
                    mavenCenterMirror
            )
    );
    private final CopyOnWriteArrayList<DependencyConfig> dependencyConfigList = new CopyOnWriteArrayList<>();

    public MHDFLibrary(Class<?> main, LoggerManager loggerManager, String relocatorPrefix, File libraryFolder) {
        instance = this;

        this.httpManager = new HttpManager();
        this.reflectionManager = new ReflectionManager();

        this.dependencyManager = new DependencyManager(
                new ReflectionClassPathAppender(main.getClassLoader())
        );

        this.loggerManager = loggerManager;

        this.relocatorPrefix = relocatorPrefix;
        this.libraryFolder = libraryFolder;

        if (!getLibraryFolder().exists()) {
            getLibraryFolder().mkdirs();
        }

        getDependencyManager().downloadDependencies(getDefaultDependencyList());
        this.relocatorManager = new RelocatorManager();

        getDependencyManager().loadDependencies(getDefaultDependencyList());
    }

    public MHDFLibrary(Class<?> main, String relocatorPrefix, File libraryFolder) {
        this(main, new LoggerManager() {
        }, relocatorPrefix, libraryFolder);
    }

    /**
     * 添加依赖配置
     *
     * @param dependencyConfig 依赖配置
     */
    public void addDependencyConfig(DependencyConfig dependencyConfig) {
        getDependencyConfigList().add(dependencyConfig);
    }

    /**
     * 开始下载依赖
     */
    public void downloadDependencies() {
        getDependencyManager().downloadDependencies(getDependencyConfigList());
    }

    /**
     * 开始加载依赖
     */
    public void loadDependencies() {
        getDependencyManager().loadDependencies(getDependencyConfigList());
    }
}
