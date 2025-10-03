package com.rainbow.plans.enums;

/**
 * 计划状态枚举
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public enum PlanStatus {
    

    DRAFT("DRAFT", "草稿"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    PAUSED("PAUSED", "暂停中"),
    DELAYED("DELAYED", "延期"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");
    
    private final String code;
    private final String description;
    
    PlanStatus(String code, String description) {
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
    public static PlanStatus fromCode(String code) {
        for (PlanStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的计划状态: " + code);
    }
    
    /**
     * 检查状态是否可以流转到目标状态
     */
    public boolean canTransitionTo(PlanStatus targetStatus) {
        switch (this) {
            case DRAFT:
                return targetStatus == IN_PROGRESS || targetStatus == CANCELLED;
            case IN_PROGRESS:
                return targetStatus == PAUSED || targetStatus == DELAYED || 
                       targetStatus == COMPLETED || targetStatus == CANCELLED;
            case PAUSED:
                return targetStatus == IN_PROGRESS || targetStatus == CANCELLED;
            case DELAYED:
                return targetStatus == IN_PROGRESS || targetStatus == CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // 已完成和已取消状态不能流转
            default:
                return false;
        }
    }
}
