package com.rainbow.plans.model.request;

import com.rainbow.plans.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 任务状态变更请求
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "任务状态变更请求")
public class TaskStatusChangeRequest {

    @NotNull(message = "状态不能为空")
    @Schema(title = "目标状态", required = true)
    private TaskStatus status;
    
    // Getter和Setter方法
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}
