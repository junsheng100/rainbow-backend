package com.rainbow.plans.enums;

/**
 * 任务状态枚举
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public enum TaskStatus {
    
    /**
     * 待开始
     */
    PENDING("PENDING", "待开始"),
    
    /**
     * 进行中
     */
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    
    /**
     * 已完成
     */
    COMPLETED("COMPLETED", "已完成"),
    
    /**
     * 已取消
     */
    CANCELLED("CANCELLED", "已取消");
    
    private final String code;
    private final String description;
    
    TaskStatus(String code, String description) {
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
    public static TaskStatus fromCode(String code) {
        for (TaskStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的任务状态: " + code);
    }
    
    /**
     * 检查状态是否可以流转到目标状态
     */
    public boolean canTransitionTo(TaskStatus targetStatus) {
        switch (this) {
            case PENDING:
                return targetStatus == IN_PROGRESS || targetStatus == CANCELLED;
            case IN_PROGRESS:
                return targetStatus == COMPLETED || targetStatus == CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // 已完成和已取消状态不能流转
            default:
                return false;
        }
    }
}
