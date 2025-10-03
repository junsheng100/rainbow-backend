package com.rainbow.plans.enums;

/**
 * 优先级枚举
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public enum Priority {
    
    /**
     * 高优先级
     */
    HIGH("HIGH", "高", 3),
    
    /**
     * 中优先级
     */
    MEDIUM("MEDIUM", "中", 2),
    
    /**
     * 低优先级
     */
    LOW("LOW", "低", 1);
    
    private final String code;
    private final String description;
    private final int level;
    
    Priority(String code, String description, int level) {
        this.code = code;
        this.description = description;
        this.level = level;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getLevel() {
        return level;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static Priority fromCode(String code) {
        for (Priority priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("未知的优先级: " + code);
    }
    
    /**
     * 比较优先级高低
     * @param other 其他优先级
     * @return true表示当前优先级更高
     */
    public boolean isHigherThan(Priority other) {
        return this.level > other.level;
    }
}
