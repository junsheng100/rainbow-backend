package com.rainbow.plans.model.request;

import com.rainbow.plans.entity.WorkPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 工作计划创建请求
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "工作计划创建请求")
public class WorkPlanCreateRequest {

    @NotNull(message = "计划信息不能为空")
    @Valid
    @Schema(title = "计划信息", required = true)
    private WorkPlan plan;

    @Schema(title = "参与者ID列表")
    private List<String> participantIds;
    
    // Getter和Setter方法
    public WorkPlan getPlan() { return plan; }
    public void setPlan(WorkPlan plan) { this.plan = plan; }
    
    public List<String> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<String> participantIds) { this.participantIds = participantIds; }
}
