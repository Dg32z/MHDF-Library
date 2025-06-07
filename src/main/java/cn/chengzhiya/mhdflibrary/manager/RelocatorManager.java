package cn.chengzhiya.mhdflibrary.manager;

import cn.chengzhiya.mhdflibrary.MHDFLibrary;
import cn.chengzhiya.mhdflibrary.entity.DependencyConfig;
import cn.chengzhiya.mhdflibrary.entity.RelocateConfig;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class RelocatorManager {
    private Constructor<?> jarRelocatorConstructor;
    private Method jarRelocatorRunMethod;

    @SneakyThrows
    public void init() {
        Class<?> jarRelocatorClass = Class.forName("me.lucko.jarrelocator.JarRelocator");
        this.jarRelocatorConstructor = jarRelocatorClass.getDeclaredConstructor(File.class, File.class, Map.class);
        this.jarRelocatorConstructor.setAccessible(true);
        this.jarRelocatorRunMethod = jarRelocatorClass.getDeclaredMethod("run");
        this.jarRelocatorRunMethod.setAccessible(true);
    }

    /**
     * 获取重定向依赖Map列表
     *
     * @return 重定向依赖Map列表
     */
    private HashMap<String, String> getRelocatorMap() {
        HashMap<String, String> relocatorMap = new HashMap<>();

        for (DependencyConfig dependencyConfig : MHDFLibrary.instance.getDependencyConfigList()) {
            if (!dependencyConfig.isEnable()) {
                continue;
            }

            RelocateConfig relocateConfig = dependencyConfig.getRelocateConfig();
            if (!relocateConfig.isEnable()) {
                continue;
            }

            String relocatorPrefix = MHDFLibrary.instance.getRelocatorPrefix();
            if (relocateConfig.isRelocatableGroupId()) {
                relocatorMap.put(dependencyConfig.getGroup(), relocatorPrefix + "." + dependencyConfig.getGroup());
            }

            for (String relocator : relocateConfig.getRelocator()) {
                relocatorMap.put(relocator, relocatorPrefix + "." + relocator);
            }
        }

        return relocatorMap;
    }

    /**
     * 获取重定位后的依赖文件
     *
     * @param original 原始依赖路径
     * @return 重定位后的依赖文件
     */
    private File getRelocatedFile(File original) {
        return new File(original.getParentFile(), "relocated-" + original.getName());
    }

    /**
     * 重新映射JAR文件中的类路径并生成新的JAR文件
     *
     * @param input 需要处理的目标JAR文件对象
     */
    @SneakyThrows
    public File relocation(File input) {
        File relocatedFile = getRelocatedFile(input);
        if (relocatedFile.exists()) {
            return relocatedFile;
        }

        if (this.jarRelocatorConstructor == null || this.jarRelocatorRunMethod == null) {
            this.init();
        }

        this.jarRelocatorRunMethod.invoke(
                this.jarRelocatorConstructor.newInstance(input, relocatedFile, getRelocatorMap())
        );

        return relocatedFile;
    }

    /**
     * 重定位依赖
     *
     * @param dependency 依赖信息实例
     * @return 重定位后的依赖路径
     */
    public File relocation(DependencyConfig dependency) {
        File file = new File(MHDFLibrary.instance.getLibraryFolder(), dependency.getFileName());

        RelocateConfig relocateConfig = dependency.getRelocateConfig();
        if (!relocateConfig.isEnable()) {
            return file;
        }

        return relocation(file);
    }
}