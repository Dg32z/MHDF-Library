package cn.chengzhiya.mhdflibrary.entity;

import lombok.Getter;

@Getter
public final class DependencyConfig {
    private final String group;
    private final String artifact;
    private final String version;
    private final RepositoryConfig repository;
    private final boolean enable;
    private final RelocateConfig relocateConfig;

    public DependencyConfig(String group, String artifact, String version, RepositoryConfig repository, boolean enable, RelocateConfig relocateConfig) {
        this.group = group;
        this.artifact = artifact;
        this.version = version;
        this.repository = repository;
        this.enable = enable;
        this.relocateConfig = relocateConfig;
    }

    public DependencyConfig(String groupId, String artifact, String version, RepositoryConfig repository, boolean enable) {
        this(groupId, artifact, version, repository, enable, new RelocateConfig(false));
    }

    public DependencyConfig(String groupId, String artifact, String version, RepositoryConfig repository, RelocateConfig relocateConfig) {
        this(groupId, artifact, version, repository, true, relocateConfig);
    }

    public DependencyConfig(String groupId, String artifact, String version, RepositoryConfig repository) {
        this(groupId, artifact, version, repository, true);
    }

    /**
     * 获取依赖文件名称
     *
     * @return 文件名称
     */
    public String getFileName() {
        return getArtifact() + "-" + getVersion() + ".jar";
    }

    /**
     * 获取依赖文件仓库路径
     *
     * @return 仓库路径
     */
    public String getMavenRepoPath() {
        return String.format("%s/%s/%s/%s",
                getGroup().replace(".", "/"),
                getArtifact(),
                getVersion(),
                getFileName()
        );
    }
}
