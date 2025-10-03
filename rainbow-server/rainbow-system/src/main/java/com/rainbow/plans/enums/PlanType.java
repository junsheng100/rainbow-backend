package com.rainbow.plans.enums;

/**
 * 计划类型枚举
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public enum PlanType {
    
    /**
     * 个人计划
     */
    PERSONAL("PERSONAL", "个人计划"),
    
    /**
     * 团队计划
     */
    TEAM("TEAM", "团队计划"),
    
    /**
     * 部门计划
     */
    DEPARTMENT("DEPARTMENT", "部门计划"),
    
    /**
     * 项目计划
     */
    PROJECT("PROJECT", "项目计划");
    
    private final String code;
    private final String description;
    
    PlanType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static PlanType fromCode(String code) {
        for (PlanType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的计划类型: " + code);
    }
}
