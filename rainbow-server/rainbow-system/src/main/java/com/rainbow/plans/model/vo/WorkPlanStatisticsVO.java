package com.rainbow.plans.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 工作计划统计视图对象
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@Schema(title = "工作计划统计视图对象")
public class WorkPlanStatisticsVO {

    @Schema(title = "总计划数")
    private long totalPlans;

    @Schema(title = "已完成计划数")
    private long completedPlans;

    @Schema(title = "进行中计划数")
    private long inProgressPlans;

    @Schema(title = "延期计划数")
    private long delayedPlans;

    @Schema(title = "完成率")
    private double completionRate;

    @Schema(title = "平均进度")
    private double averageProgress;

    @Schema(title = "本月新增计划数")
    private long monthlyNewPlans;

    @Schema(title = "本月完成计划数")
    private long monthlyCompletedPlans;

    @Schema(title = "本周新增计划数")
    private long weeklyNewPlans;

    @Schema(title = "本周完成计划数")
    private long weeklyCompletedPlans;

    @Schema(title = "今日新增计划数")
    private long todayNewPlans;

    @Schema(title = "今日完成计划数")
    private long todayCompletedPlans;
}
