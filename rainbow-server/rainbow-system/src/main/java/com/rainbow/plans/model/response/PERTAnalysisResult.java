package com.rainbow.plans.model.response;

import com.rainbow.plans.entity.WorkPlanTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * PERT分析结果
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "PERT分析结果")
public class PERTAnalysisResult {

    @Schema(title = "关键路径任务列表")
    private List<WorkPlanTask> criticalPath;

    @Schema(title = "项目总工期（天）")
    private Integer projectDuration;

    @Schema(title = "总任务数")
    private Integer totalTasks;

    @Schema(title = "关键任务数")
    private Integer criticalTasks;

    @Schema(title = "平均浮动时间（天）")
    private Double averageFloat;

    @Schema(title = "风险等级", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private String riskLevel;

    @Schema(title = "项目开始时间")
    private String projectStartDate;

    @Schema(title = "项目结束时间")
    private String projectEndDate;

    @Schema(title = "关键路径长度")
    private Integer criticalPathLength;

    @Schema(title = "非关键任务数")
    private Integer nonCriticalTasks;

    @Schema(title = "最大浮动时间")
    private Integer maxFloat;

    @Schema(title = "最小浮动时间")
    private Integer minFloat;

    public PERTAnalysisResult() {
        this.riskLevel = "LOW";
        this.averageFloat = 0.0;
    }
}
