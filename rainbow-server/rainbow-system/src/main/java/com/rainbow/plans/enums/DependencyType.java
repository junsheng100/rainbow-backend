package com.rainbow.plans.enums;

/**
 * 任务依赖类型枚举
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public enum DependencyType {
    /**
     * 完成-开始 (Finish-to-Start)
     * 前置任务完成后，后续任务才能开始
     */
    FS("完成-开始", "Finish-to-Start"),
    
    /**
     * 开始-开始 (Start-to-Start)
     * 前置任务开始后，后续任务才能开始
     */
    SS("开始-开始", "Start-to-Start"),
    
    /**
     * 完成-完成 (Finish-to-Finish)
     * 前置任务完成后，后续任务才能完成
     */
    FF("完成-完成", "Finish-to-Finish"),
    
    /**
     * 开始-完成 (Start-to-Finish)
     * 前置任务开始后，后续任务才能完成
     */
    SF("开始-完成", "Start-to-Finish");

    private final String description;
    private final String englishName;

    DependencyType(String description, String englishName) {
        this.description = description;
        this.englishName = englishName;
    }

    public String getDescription() {
        return description;
    }

    public String getEnglishName() {
        return englishName;
    }
}
