package com.rainbow.plans.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.rainbow.base.annotation.ResultDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.model.request.ProgressUpdateRequest;
import com.rainbow.plans.model.request.TaskStatusChangeRequest;
import com.rainbow.plans.model.response.PERTAnalysisResult;
import com.rainbow.plans.service.WorkPlanTaskService;
import com.rainbow.plans.service.WorkTaskDependencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 计划任务控制器
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/workplan/task")
@Tag(name = "计划任务管理", description = "计划任务的增删改查和状态管理")
public class WorkPlanTaskController extends BaseController<WorkPlanTask, String, WorkPlanTaskService> {

  @Autowired
  private WorkTaskDependencyService workTaskDependencyService;

  @Operation(description = "任务列表查询")
  @RestResponse
  @GetMapping("/list/{planId}")
  public Result<List<WorkPlanTask>> findTaskByPlanId(@PathVariable(name = "planId") String planId) {
    List<WorkPlanTask> data = service.findTaskByPlanId(planId);
    return Result.success(data);
  }


  @PutMapping("/{id}/progress")
  @Operation(summary = "更新任务进度", description = "更新任务的完成进度")
  @OperLog(title = "更新任务进度")
  public Result<Void> updateProgress(@PathVariable String id, @RequestBody @Valid ProgressUpdateRequest request) {
    service.updateTaskProgress(id, request.getProgress());
    return Result.success();
  }

  @PutMapping("/{id}/assign")
  @Operation(summary = "分配任务", description = "将任务分配给指定用户")
  @OperLog(title = "分配任务")
  public Result<Void> assignTask(
          @PathVariable String id,
          @Parameter(description = "分配人ID") @RequestParam String assigneeId) {
    service.assignTask(id, assigneeId);
    return Result.success();
  }

  @PutMapping("/{id}/status")
  @Operation(summary = "变更任务状态", description = "变更任务的状态")
  @OperLog(title = "变更任务状态")
  public Result<Void> changeStatus(@PathVariable String id, @RequestBody @Valid TaskStatusChangeRequest request) {
    service.changeTaskStatus(id, request.getStatus());
    return Result.success();
  }


  @ResultDisplay({"id","taskName","sortOrder"})
  @GetMapping("/plan/{planId}")
  @Operation(summary = "获取计划任务", description = "获取指定计划的所有任务")
  public Result<List<WorkPlanTask>> getTasksByPlanId(@PathVariable String planId) {
    List<WorkPlanTask> tasks = service.getTasksByPlanId(planId);
    return Result.success(tasks);
  }

  @GetMapping("/milestone/{planId}")
  @Operation(summary = "获取里程碑任务", description = "获取指定计划的里程碑任务")
  public Result<List<WorkPlanTask>> getMilestoneTasks(@PathVariable String planId) {
    List<WorkPlanTask> tasks = service.getMilestoneTasks(planId);
    return Result.success(tasks);
  }

  @GetMapping("/my")
  @Operation(summary = "获取我的任务", description = "获取分配给当前用户的任务")
  public Result<List<WorkPlanTask>> getMyTasks() {
    String userId = getLoginUser().getUserId();
    List<WorkPlanTask> tasks = service.getUserTasks(userId);
    return Result.success(tasks);
  }

  @GetMapping("/completion-rate/{planId}")
  @Operation(summary = "获取任务完成率", description = "获取指定计划的任务完成率")
  public Result<Double> getTaskCompletionRate(@PathVariable String planId) {
    double completionRate = service.getTaskCompletionRate(planId);
    return Result.success(completionRate);
  }

  @PutMapping("/order")
  @Operation(summary = "更新任务排序", description = "批量更新任务的排序")
  @OperLog(title = "更新任务排序")
  public Result<Void> updateTaskOrder(@RequestBody List<String> taskIds) {
    service.updateTaskOrder(taskIds);
    return Result.success();
  }

  // PERT/CPM相关接口

  @PostMapping("/{planId}/critical-path")
  @Operation(summary = "计算关键路径", description = "计算计划的关键路径")
  @OperLog("计算关键路径")
  @RestResponse
  public Result<List<WorkPlanTask>> calculateCriticalPath(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    List<WorkPlanTask> criticalPath = workTaskDependencyService.calculateCriticalPath(planId);
    return Result.success(criticalPath);
  }

//  @GetMapping("/{planId}/pert-analysis")
//  @Operation(summary = "获取PERT分析", description = "获取计划的PERT分析结果")
//  @RestResponse
//  public Result<PERTAnalysisResult> getPERTAnalysis(
//          @Parameter(description = "计划ID") @PathVariable String planId) {
//    PERTAnalysisResult analysis = workTaskDependencyService.getPERTAnalysis(planId);
//    return Result.success(analysis);
//  }

  @PostMapping("/{planId}/calculate-times")
  @Operation(summary = "计算任务时间", description = "计算任务的最早和最晚时间")
  @OperLog("计算任务时间")
  @RestResponse
  public Result<Void> calculateTaskTimes(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    workTaskDependencyService.calculateTaskTimes(planId);
    return Result.success();
  }

  @GetMapping("/{planId}/critical-tasks")
  @Operation(summary = "获取关键任务", description = "获取计划中的关键任务")
  @RestResponse
  public Result<List<WorkPlanTask>> getCriticalTasks(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    List<WorkPlanTask> criticalTasks = workTaskDependencyService.calculateCriticalPath(planId);
    return Result.success(criticalTasks);
  }

  @GetMapping("/{planId}/start-tasks")
  @Operation(summary = "获取起始任务", description = "获取计划中没有前置任务的起始任务")
  @RestResponse
  public Result<List<WorkPlanTask>> getStartTasks(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    // 这里需要从repository直接查询，因为service层没有这个方法
    // 暂时返回空列表，后续可以扩展service层
    return Result.success(new ArrayList<>());
  }

  @GetMapping("/{planId}/end-tasks")
  @Operation(summary = "获取结束任务", description = "获取计划中没有后续任务的结束任务")
  @RestResponse
  public Result<List<WorkPlanTask>> getEndTasks(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    // 这里需要从repository直接查询，因为service层没有这个方法
    // 暂时返回空列表，后续可以扩展service层
    return Result.success(new ArrayList<>());
  }
}
