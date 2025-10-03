package com.rainbow.plans.enums;

/**
 * 参与者角色枚举
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public enum ParticipantRole {
    

    CREATOR("CREATOR", "创建者"),
    OWNER("OWNER", "负责人"),
    MEMBER("MEMBER", "成员"),
    OBSERVER("OBSERVER", "观察者");
    
    private final String code;
    private final String description;
    
    ParticipantRole(String code, String description) {
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
    public static ParticipantRole fromCode(String code) {
        for (ParticipantRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的参与者角色: " + code);
    }
    
    /**
     * 检查角色是否有编辑权限
     */
    public boolean hasEditPermission() {
        return this == CREATOR || this == OWNER;
    }
    
    /**
     * 检查角色是否有查看权限
     */
    public boolean hasViewPermission() {
        return true; // 所有角色都有查看权限
    }
}
