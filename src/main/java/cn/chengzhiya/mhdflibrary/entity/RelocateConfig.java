package cn.chengzhiya.mhdflibrary.entity;

import lombok.Getter;

@Getter
public final class RelocateConfig {
    private final boolean enable;
    private final boolean relocatableGroupId;
    private final String[] relocator;

    public RelocateConfig(boolean enable, boolean relocatableGroupId, String... relocator) {
        this.enable = enable;
        this.relocatableGroupId = relocatableGroupId;
        this.relocator = relocator;
    }

    public RelocateConfig(boolean relocatable) {
        this(relocatable, true);
    }
}
