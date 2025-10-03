package com.rainbow.plans.model.request;

import com.rainbow.plans.enums.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 计划状态变更请求
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "计划状态变更请求")
public class PlanStatusChangeRequest {

    @NotNull(message = "状态不能为空")
    @Schema(title = "目标状态", required = true)
    private PlanStatus status;

    @Schema(title = "状态变更原因")
    private String reason;
    
    // Getter和Setter方法
    public PlanStatus getStatus() { return status; }
    public void setStatus(PlanStatus status) { this.status = status; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
