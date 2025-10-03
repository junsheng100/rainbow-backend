package com.rainbow.plans.controller;

import java.util.List;

import javax.validation.Valid;

import cn.hutool.core.util.IdUtil;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.utils.RandomId;
import org.springframework.web.bind.annotation.*;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.annotation.RestResponse;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.plans.entity.WorkPlan;
import com.rainbow.plans.enums.PlanType;
import com.rainbow.plans.model.request.PlanStatusChangeRequest;
import com.rainbow.plans.model.request.ProgressUpdateRequest;
import com.rainbow.plans.model.request.WorkPlanRequest;
import com.rainbow.plans.service.WorkPlanService;
import com.rainbow.user.entity.UserInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工作计划控制器
 *
 * @author rainbow
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/workplan")
@Tag(name = "工作计划管理", description = "工作计划的增删改查和状态管理")
public class WorkPlanController extends BaseController<WorkPlan, String, WorkPlanService> {

  @NoRepeatSubmit
  @OperLog("创建工作计划")
  @Operation(description = "创建工作计划")
  @RestResponse
  @PostMapping("/create")
  public Result<WorkPlan> createPlan(@RequestBody WorkPlanRequest request) {
    WorkPlan data = service.createPlan(request);
    return Result.success(data);
  }

  @OperLog
  @Operation(summary = "logo上传")
  @PostMapping("/logo/upload")
  @ResponseBody
  public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    try {

      String srcName = multipartFile.getOriginalFilename();
      String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
      String localPath = "/plans/logo/" + RandomId.generateShortUuid() + "." + suffix;

      service.uploadFile(multipartFile, localPath);

      return Result.success(localPath);

    } catch (Exception e) {
      log.error("文件上传失败", e);
      return Result.error("文件上传失败");
    }
  }


  @NoRepeatSubmit
  @OperLog("编辑工作计划")
  @Operation(description = "编辑工作计划")
  @RestResponse
  @PostMapping("/update")
  public Result<WorkPlan> editPlan(@RequestBody WorkPlanRequest request) {
    WorkPlan data = service.editPlan(request);
    return Result.success(data);
  }

  @Operation(description = "工作计划人员信息")
  @RestResponse
  @GetMapping("/user/{planId}")
  public Result<List<UserInfo>> findUserByPlanId(@PathVariable(name = "planId") String planId) {
    List<UserInfo> data = service.findUserByPlanId(planId);

    return Result.success(data);
  }



  @PutMapping("/{id}/progress")
  @Operation(summary = "更新计划进度", description = "更新工作计划的完成进度")
  @OperLog(title = "更新计划进度")
  public Result<Void> updateProgress(@PathVariable String id, @RequestBody @Valid ProgressUpdateRequest request) {
    service.updateProgress(id, request.getProgress());
    return Result.success();
  }

  @PutMapping("/{id}/assign")
  @Operation(summary = "分配计划", description = "将计划分配给指定用户")
  @OperLog(title = "分配计划")
  public Result<Void> assignPlan(
          @PathVariable String id,
          @Parameter(description = "分配人ID") @RequestParam String assigneeId) {
    service.assignPlan(id, assigneeId);
    return Result.success();
  }

  @PutMapping("/{id}/status")
  @Operation(summary = "变更计划状态", description = "变更工作计划的状态")
  @OperLog(title = "变更计划状态")
  public Result<Void> changeStatus(@PathVariable String id, @RequestBody @Valid PlanStatusChangeRequest request) {
    service.changeStatus(id, request.getStatus(), request.getReason());
    return Result.success();
  }

  @PostMapping("/{id}/copy")
  @Operation(summary = "复制计划", description = "复制现有计划创建新计划")
  @OperLog(title = "复制计划")
  public Result<WorkPlan> copyPlan(
          @PathVariable String id,
          @Parameter(description = "新计划名称") @RequestParam String newPlanName) {
    WorkPlan copiedPlan = service.copyPlan(id, newPlanName);
    return Result.success(copiedPlan);
  }

  @GetMapping("/my")
  @Operation(summary = "获取我的计划", description = "获取当前用户相关的计划")
  public Result<List<WorkPlan>> getMyPlans(
          @Parameter(description = "计划类型") @RequestParam(required = false) PlanType planType) {
    String userId = getLoginUser().getUserId();
    List<WorkPlan> plans = service.getUserPlans(userId, planType);
    return Result.success(plans);
  }

  @GetMapping("/dept/{deptId}")
  @Operation(summary = "获取部门计划", description = "获取指定部门的计划")
  public Result<List<WorkPlan>> getDeptPlans(@PathVariable Long deptId) {
    List<WorkPlan> plans = service.getDeptPlans(deptId);
    return Result.success(plans);
  }

  @GetMapping("/statistics")
  @Operation(summary = "获取计划统计", description = "获取当前用户的计划统计信息")
  public Result<WorkPlanService.PlanStatistics> getPlanStatistics(
          @Parameter(description = "时间范围") @RequestParam(required = false) String timeRange) {
    String userId = getLoginUser().getUserId();
    WorkPlanService.PlanStatistics statistics = service.getPlanStatistics(userId, timeRange);
    return Result.success(statistics);
  }

  @GetMapping("/expiring")
  @Operation(summary = "获取即将到期计划", description = "获取即将到期的计划")
  public Result<List<WorkPlan>> getExpiringPlans(
          @Parameter(description = "提前天数") @RequestParam(defaultValue = "3") int days) {
    List<WorkPlan> plans = service.getExpiringPlans(days);
    return Result.success(plans);
  }

  @GetMapping("/delayed")
  @Operation(summary = "获取延期计划", description = "获取已延期的计划")
  public Result<List<WorkPlan>> getDelayedPlans() {
    List<WorkPlan> plans = service.getDelayedPlans();
    return Result.success(plans);
  }

  @PostMapping("/{id}/participants")
  @Operation(summary = "添加参与者", description = "为计划添加参与者")
  @OperLog(title = "添加计划参与者")
  public Result<Void> addParticipant(
          @PathVariable String id,
          @Parameter(description = "用户ID") @RequestParam String userId,
          @Parameter(description = "角色") @RequestParam String role) {
    service.addParticipant(id, userId, role);
    return Result.success();
  }

  @DeleteMapping("/{id}/participants/{userId}")
  @Operation(summary = "移除参与者", description = "从计划中移除参与者")
  @OperLog(title = "移除计划参与者")
  public Result<Void> removeParticipant(@PathVariable String id, @PathVariable String userId) {
    service.removeParticipant(id, userId);
    return Result.success();
  }

  @GetMapping("/{id}/permission")
  @Operation(summary = "检查权限", description = "检查用户对计划的权限")
  public Result<Boolean> checkPermission(
          @PathVariable String id,
          @Parameter(description = "权限类型") @RequestParam String permission) {
    String userId = getLoginUser().getUserId();
    boolean hasPermission = service.hasPermission(id, userId, permission);
    return Result.success(hasPermission);
  }

}
