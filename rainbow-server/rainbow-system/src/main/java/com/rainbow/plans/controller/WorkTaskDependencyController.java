package com.rainbow.plans.controller;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.plans.entity.WorkTaskDependency;
import com.rainbow.plans.model.response.DependencyResponse;
import com.rainbow.plans.model.response.PERTAnalysisResult;
import com.rainbow.plans.service.WorkTaskDependencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务依赖关系控制器
 *
 * @author rainbow
 * @since 2024-01-01
 */

@Slf4j
@RestController
@RequestMapping("/workplan/dependency")
@Tag(name = "工序管理", description = "工序管理")
public class WorkTaskDependencyController extends BaseController<WorkTaskDependency, String, WorkTaskDependencyService> {


  @GetMapping("/plan/{planId}")
  @Operation(summary = "获取计划依赖关系", description = "根据计划ID获取所有任务依赖关系")
  @RestResponse
  public Result<List<WorkTaskDependency>> getDependenciesByPlanId(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    List<WorkTaskDependency> dependencies = service.getDependenciesByPlanId(planId);
    return Result.success(dependencies);
  }


  @GetMapping("/pert/{planId}")
  @Operation(summary = "获取PERT图数据", description = "根据计划ID获取PERT图数据")
  @RestResponse
  public Result<List<DependencyResponse>> findPertCpmByPlanId (
          @Parameter(description = "计划ID") @PathVariable String planId) {
    List<DependencyResponse> dependencies = service.getPertCpmByPlanId(planId);
    return Result.success(dependencies);
  }


  @GetMapping("/pert-analysis/{planId}")
  @Operation(summary = "获取PERT分析结果", description = "获取PERT分析结果")
  @RestResponse
  public Result<PERTAnalysisResult> getPERTAnalysisResult (@Parameter(description = "计划ID") @PathVariable String planId) {
    PERTAnalysisResult analysisResult = service.getPERTAnalysis(planId);
    return Result.success(analysisResult);
  }


  @PostMapping("/validate/{planId}")
  @Operation(summary = "验证依赖关系", description = "验证计划中的依赖关系是否有效")
  @RestResponse
  public Result<DependencyResponse> validateDependencies(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    boolean isValid = service.validateDependencies(planId);
    DependencyResponse response = new DependencyResponse(planId, null, null, isValid);
    return Result.success(response);
  }

  @GetMapping("/validate")
  @Operation(summary = "验证依赖关系", description = "验证计划中的依赖关系是否有效")
  @RestResponse
  public Result<DependencyResponse> validate(
          @Parameter(description = "计划ID") @RequestParam(name = "planId") String planId,
          @Parameter(description = "前置任务ID") @RequestParam(name = "prevTaskId", required = false) String prevTaskId,
          @Parameter(description = "后置任务ID") @RequestParam(name = "postTaskId", required = false) String postTaskId) {


    boolean isValid = service.validateDependencies(planId, prevTaskId, postTaskId);
    DependencyResponse response = new DependencyResponse(planId, prevTaskId, postTaskId, isValid);

    return Result.success(response);
  }

  @PostMapping("/check-circular/{planId}")
  @Operation(summary = "检查循环依赖", description = "检查计划中是否存在循环依赖")
  @RestResponse
  public Result<Boolean> checkCircularDependency(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    boolean hasCircular = service.hasCircularDependency(planId);
    return Result.success(hasCircular);
  }


  @PostMapping("/recalculate/{planId}")
  @Operation(summary = "重新计算PERT数据", description = "重新计算计划的PERT数据")
//  @OperLog("重新计算PERT数据")
  @RestResponse
  public Result<Void> recalculatePlan(
          @Parameter(description = "计划ID") @PathVariable String planId) {
    service.recalculatePlan(planId);
    return Result.success();
  }
}
