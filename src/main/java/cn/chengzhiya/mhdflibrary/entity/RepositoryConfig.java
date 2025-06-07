package cn.chengzhiya.mhdflibrary.entity;

import lombok.Getter;

@Getter
public final class RepositoryConfig {
    private final String url;
    private final String id;

    public RepositoryConfig(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public RepositoryConfig(String url) {
        this(url, url);
    }

    /**
     * 获取依赖文件仓库地址
     *
     * @param dependencyConfig 依赖配置实例
     * @return 仓库地址
     */
    public String getDependencyUrl(DependencyConfig dependencyConfig) {
        return getUrl() + dependencyConfig.getMavenRepoPath();
    }
}
