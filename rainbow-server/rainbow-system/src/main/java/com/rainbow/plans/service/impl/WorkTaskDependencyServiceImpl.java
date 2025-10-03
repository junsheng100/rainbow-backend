package com.rainbow.plans.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.entity.WorkTaskDependency;
import com.rainbow.plans.model.response.DependencyResponse;
import com.rainbow.plans.model.response.PERTAnalysisResult;
import com.rainbow.plans.resource.WorkPlanTaskDao;
import com.rainbow.plans.resource.WorkTaskDependencyDao;
import com.rainbow.plans.service.WorkTaskDependencyService;
import com.rainbow.plans.utils.DependencyNode;
import com.rainbow.plans.utils.DependencyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工序流程服务实现类 - 重新设计的任务前置依赖管理
 * 取消后续任务概念，采用当前任务的前置依赖模式
 *
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.service.impl
 * @Filename：WorkTaskDependencyServiceImpl
 * @Date：2025/9/26 14:21
 * @Describe: 工序流程管理，支持关键路径计算和PERT分析
 */
@Slf4j
@Service
public class WorkTaskDependencyServiceImpl extends BaseServiceImpl<WorkTaskDependency, String, WorkTaskDependencyDao> implements WorkTaskDependencyService {


  @Autowired
  private WorkPlanTaskDao taskDao;
  @Autowired
  private WorkTaskDependencyDao dependencyDao;

  @Override
  @Transactional
  public List<WorkPlanTask> calculateCriticalPath(String planId) {
    log.info("开始计算计划 {} 的关键路径", planId);

    // 1. 数据验证
    if (planId == null || planId.trim().isEmpty()) {
      log.warn("计划ID不能为空");
      return Collections.emptyList();
    }

    // 2. 获取所有任务和依赖关系
    List<WorkPlanTask> tasks = taskDao.findByPlanId(planId);
    List<WorkTaskDependency> dependencies = dependencyDao.findByPlanId(planId);

    if (tasks.isEmpty()) {
      log.warn("计划 {} 没有任务", planId);
      return Collections.emptyList();
    }

    // 3. 检查循环依赖
    if (detectCircularDependency(dependencies)) {
      log.error("计划 {} 存在循环依赖，无法计算关键路径", planId);
      throw new BizException("存在循环依赖，无法计算关键路径");
    }

    // 4. 初始化任务数据
    initializeTaskDates(tasks);

    // 5. 计算最早时间
    calculateEarlyTimes(tasks, dependencies);

    // 6. 计算最晚时间
    calculateLateTimes(tasks, dependencies);

    // 7. 计算浮动时间和关键路径
    List<WorkPlanTask> criticalPath = calculateFloatAndCriticalPath(tasks);

    // 8. 验证结果
    if (!validateCalculationResults(tasks)) {
      log.warn("计算结果验证失败，可能存在数据问题");
    }

    // 9. 保存计算结果
    saveCalculationResults(tasks);

    log.info("计划 {} 关键路径计算完成，关键任务数: {}", planId, criticalPath.size());
    return criticalPath;
  }

  @Override
  @Transactional
  public void calculateTaskTimes(String planId) {
    log.info("开始计算计划 {} 的任务时间", planId);
    calculateCriticalPath(planId);
  }

  @Override
  public PERTAnalysisResult getPERTAnalysis(String planId) {
    log.info("获取计划 {} 的PERT分析结果", planId);

    List<WorkPlanTask> tasks = taskDao.findByPlanId(planId);
    List<WorkPlanTask> criticalPath = taskDao.findCriticalPathTasks(planId);

    PERTAnalysisResult result = new PERTAnalysisResult();
    result.setCriticalPath(criticalPath);
    result.setTotalTasks(tasks.size());
    result.setCriticalTasks(criticalPath.size());
    result.setNonCriticalTasks(tasks.size() - criticalPath.size());

    // 计算项目工期
    if (!criticalPath.isEmpty()) {
      WorkPlanTask lastTask = criticalPath.get(criticalPath.size() - 1);
      if (lastTask.getEarlyFinishDate() != null) {
        result.setProjectEndDate(lastTask.getEarlyFinishDate().toString());
        result.setProjectDuration((int) ChronoUnit.DAYS.between(
                criticalPath.get(0).getEarlyStartDate() != null ? criticalPath.get(0).getEarlyStartDate() : LocalDate.now(),
                lastTask.getEarlyFinishDate()
        ));
      }
    }

    // 计算平均浮动时间
    List<WorkPlanTask> floatTasks = tasks.stream()
            .filter(task -> task.getFloatDays() != null && task.getFloatDays() > 0)
            .collect(Collectors.toList());

    if (!floatTasks.isEmpty()) {
      double avgFloat = floatTasks.stream()
              .mapToInt(WorkPlanTask::getFloatDays)
              .average()
              .orElse(0.0);
      result.setAverageFloat(avgFloat);

      // 计算最大最小浮动时间
      result.setMaxFloat(floatTasks.stream().mapToInt(WorkPlanTask::getFloatDays).max().orElse(0));
      result.setMinFloat(floatTasks.stream().mapToInt(WorkPlanTask::getFloatDays).min().orElse(0));
    }

    // 计算风险等级
    result.setRiskLevel(calculateRiskLevel(result));

    return result;
  }

  @Override
  public boolean validateDependencies(String planId) {
    return !hasCircularDependency(planId);
  }

  @Override
  public boolean hasCircularDependency(String planId) {
    List<WorkTaskDependency> dependencies = dependencyDao.findByPlanId(planId);
    return detectCircularDependency(dependencies);
  }


  @Override
  public WorkTaskDependency store(@Valid WorkTaskDependency entity) {
    if (validate(entity)) {
      saveDependency(entity);
      return entity;
    }
    return null;
  }


  @Override
  public WorkTaskDependency save(@Valid WorkTaskDependency entity) {

    return store(entity);
  }


  @Override
  public Boolean delete(String id) {
    deleteDependency(id);
    return true;
  }

  /**
   * 创建工序依赖关系
   *
   * @param dependency 依赖关系实体
   * @return 创建的依赖关系
   */
  @Override
  @Transactional
  public WorkTaskDependency saveDependency(WorkTaskDependency dependency) {
    // 验证任务不能依赖自己


    validate(dependency);

    // 检查是否已存在相同的依赖关系
//    List<WorkTaskDependency> existingDeps = dependencyDao.findByPostTaskId(dependency.getPostTaskId());
//    boolean exists = existingDeps.stream()
//            .anyMatch(d -> d.getPrevTaskId().equals(dependency.getPrevTaskId()));
//    if (exists) {
//      throw new BizException("依赖关系已存在");
//    }
//
//    // 验证不会产生循环依赖
//    if (wouldCreateCircularDependency(dependency)) {
//      throw new BizException("创建此依赖关系会导致循环依赖");
//    }
    List<WorkTaskDependency> list = dependencyDao.findByPlanId(dependency.getPlanId());
    if (CollectionUtils.isNotEmpty(list)) {
      boolean exists = list.stream()
              .anyMatch(d -> DependencyUtils.isExists(dependency, d));

      if (!exists) {
        list.add(dependency);
      }
      list.sort((d1, d2) -> d1.getSequenceOrder() - d2.getSequenceOrder());

      List<DependencyNode> nodeList = list.stream()
              .map(d -> new DependencyNode(d.getPostTaskId()))
              .collect(Collectors.toList());

      boolean flag = DependencyUtils.hasCycle(nodeList);
      if (flag)
        throw new BizException("保存此依赖关系会导致循环依赖");
    }

    WorkTaskDependency savedDependency = dependencyDao.store(dependency);

    // 重新计算关键路径
//    recalculatePlan(dependency.getPlanId());

    log.info("创建工序依赖关系成功: 任务[{}]依赖于[{}]",
            dependency.getPostTaskId(), dependency.getPrevTaskId());
    return savedDependency;
  }


  /**
   * 检查是否会产生循环依赖
   */
  private boolean wouldCreateCircularDependency(WorkTaskDependency newDependency) {
    List<WorkTaskDependency> allDependencies = new ArrayList<>(dependencyDao.findByPlanId(newDependency.getPlanId()));
    allDependencies.add(newDependency);
    return detectCircularDependency(allDependencies);
  }

  /**
   * 计算工序顺序号
   */
  private Integer calculateSequenceOrder(WorkTaskDependency dependency) {
    List<WorkTaskDependency> taskDependencies = dependencyDao.findByPostTaskId(dependency.getPostTaskId());
    if (taskDependencies.isEmpty()) {
      return 1; // 第一个依赖
    }

    return taskDependencies.stream()
            .mapToInt(d -> d.getSequenceOrder() != null ? d.getSequenceOrder() : 0)
            .max()
            .orElse(0) + 1;
  }

  /**
   * 新模式下的拓扑排序 - 基于前置依赖
   */
  private List<WorkPlanTask> topologicalSortForNewModel(List<WorkPlanTask> tasks, List<WorkTaskDependency> dependencies) {
    Map<String, List<String>> graph = new HashMap<>();
    Map<String, Integer> inDegree = new HashMap<>();

    // 初始化
    for (WorkPlanTask task : tasks) {
      graph.put(task.getId(), new ArrayList<>());
      inDegree.put(task.getId(), 0);
    }

    // 构建图：从前置任务指向当前任务
    for (WorkTaskDependency dep : dependencies) {
      graph.get(dep.getPrevTaskId()).add(dep.getPostTaskId());
      inDegree.put(dep.getPostTaskId(), inDegree.get(dep.getPostTaskId()) + 1);
    }

    // 拓扑排序
    Queue<String> queue = new LinkedList<>();
    for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.offer(entry.getKey());
      }
    }

    List<WorkPlanTask> result = new ArrayList<>();
    Map<String, WorkPlanTask> taskMap = tasks.stream()
            .collect(Collectors.toMap(WorkPlanTask::getId, task -> task));

    while (!queue.isEmpty()) {
      String taskId = queue.poll();
      result.add(taskMap.get(taskId));

      for (String dependentTask : graph.get(taskId)) {
        inDegree.put(dependentTask, inDegree.get(dependentTask) - 1);
        if (inDegree.get(dependentTask) == 0) {
          queue.offer(dependentTask);
        }
      }
    }

    return result;
  }

  @Override
  public void deleteDependency(String dependencyId) {
    WorkTaskDependency dependency = dependencyDao.get(dependencyId);
    if (null == dependency)
      throw new BizException("依赖关系不存在");

    String prevTaskId = dependency.getPrevTaskId();
    String postTaskId = dependency.getPostTaskId();

    String planId = dependency.getPlanId();
    dependencyDao.remove(dependencyId);

    // 重新计算PERT数据
    recalculatePlan(planId);

    log.info("删除依赖关系成功: {}", dependencyId);
  }

  @Override
  public List<WorkTaskDependency> getDependenciesByPlanId(String planId) {
    return dependencyDao.findByPlanId(planId);
  }

  @Override
  @Transactional
  public void recalculatePlan(String planId) {
    log.info("重新计算计划 {} 的PERT数据", planId);
    calculateCriticalPath(planId);
  }

  @Override
  public boolean validateDependencies(String planId, String prevTaskId, String postTaskId) {
    if (StringUtils.isBlank(planId))
      throw new BizException("计划ID不能为空");
    List<WorkTaskDependency> list = dependencyDao.findByPlanId(planId);
    if (CollectionUtils.isEmpty(list))
      return false;
//    Long count = list.stream().filter(dependency -> dependency.getPrevTaskId().equals(prevTaskId) && dependency.getPostTaskId().equals(postTaskId)).count();
//    if (count > 0)
//      return true;

    List<DependencyNode> nodeList = list.stream().map(dependency -> new DependencyNode(dependency.getPrevTaskId(), dependency.getPostTaskId())).collect(Collectors.toList());
    boolean hasCycle = DependencyUtils.hasCycle(nodeList);

    return hasCycle;
  }

  @Override
  public List<DependencyResponse> getPertCpmByPlanId(String planId) {
    List<WorkTaskDependency> dependencies = getDependenciesByPlanId(planId);
    convertCollection(dependencies);


    List<DependencyResponse> responseList = dependencies.stream().map(dependency -> {
      DependencyResponse response = new DependencyResponse();
      BeanUtils.copyProperties(dependency, response, CommonUtils.getNullPropertyNames(dependency));
      return response;
    }).collect(Collectors.toList());

    responseList.sort((r1, r2) -> r1.getSequenceOrder() - r2.getSequenceOrder());

    return responseList;
  }


  /**
   * 计算最早时间 - 新模式：基于前置依赖
   */
  private void calculateEarlyTimes(List<WorkPlanTask> tasks, List<WorkTaskDependency> dependencies) {
    Map<String, WorkPlanTask> taskMap = tasks.stream()
            .collect(Collectors.toMap(WorkPlanTask::getId, task -> task));

    // 构建依赖关系映射：当前任务 -> 其前置依赖列表
    Map<String, List<WorkTaskDependency>> dependencyMap = dependencies.stream()
            .collect(Collectors.groupingBy(WorkTaskDependency::getPostTaskId));

    // 拓扑排序
    List<WorkPlanTask> sortedTasks = topologicalSortForNewModel(tasks, dependencies);

    // 检查拓扑排序结果的完整性
    if (sortedTasks.size() != tasks.size()) {
      log.error("拓扑排序结果不完整，可能存在循环依赖");
      throw new BizException("存在循环依赖，无法计算最早时间");
    }

    for (WorkPlanTask task : sortedTasks) {
      List<WorkTaskDependency> prerequisites = dependencyMap.getOrDefault(task.getId(), Collections.emptyList());
      LocalDate maxEarlyStart = null;

      // 找到所有前置任务中最晚的完成时间
      for (WorkTaskDependency dep : prerequisites) {
        WorkPlanTask prerequisite = taskMap.get(dep.getPrevTaskId());
        if (prerequisite != null && prerequisite.getEarlyFinishDate() != null) {
          LocalDate prerequisiteEnd = prerequisite.getEarlyFinishDate();
          // 添加滞后时间
          int lagDays = dep.getLagDays() != null ? dep.getLagDays() : 0;
          LocalDate earliestStart = prerequisiteEnd.plusDays(lagDays);

          if (maxEarlyStart == null || earliestStart.isAfter(maxEarlyStart)) {
            maxEarlyStart = earliestStart;
          }
        }
      }

      // 设置最早开始时间
      if (maxEarlyStart != null) {
        task.setEarlyStartDate(maxEarlyStart);
      } else {
        // 没有前置任务，使用计划开始时间
        LocalDate startDate = task.getPlanStartDate() != null ? task.getPlanStartDate() : LocalDate.now();
        task.setEarlyStartDate(startDate);
      }

      // 计算最早完成时间
      int duration = getTaskDuration(task);
      task.setEarlyFinishDate(task.getEarlyStartDate().plusDays(Math.max(0, duration - 1)));

      log.debug("任务 {} 的最早时间：{} - {}",
              task.getId(),
              task.getEarlyStartDate(),
              task.getEarlyFinishDate());
    }
  }

  /**
   * 计算最晚时间 - 新模式：基于前置依赖
   */
  private void calculateLateTimes(List<WorkPlanTask> tasks, List<WorkTaskDependency> dependencies) {
    Map<String, WorkPlanTask> taskMap = tasks.stream()
            .collect(Collectors.toMap(WorkPlanTask::getId, task -> task));

    // 构建反向映射：前置任务 -> 依赖于它的任务列表
    Map<String, List<WorkTaskDependency>> successorMap = dependencies.stream()
            .collect(Collectors.groupingBy(WorkTaskDependency::getPrevTaskId));

    // 找到项目结束时间
    LocalDate projectEndTime = tasks.stream()
            .map(WorkPlanTask::getEarlyFinishDate)
            .filter(Objects::nonNull)
            .max(LocalDate::compareTo)
            .orElse(LocalDate.now());

    // 逆拓扑排序计算最晚时间
    List<WorkPlanTask> sortedTasks = new ArrayList<>(tasks);
    Collections.reverse(sortedTasks);

    for (WorkPlanTask task : sortedTasks) {
      List<WorkTaskDependency> successors = successorMap.getOrDefault(task.getId(), Collections.emptyList());
      LocalDate minLateStart = null;

      if (successors.isEmpty()) {
        // 没有后续任务，最晚完成时间等于项目结束时间
        task.setLateFinishDate(projectEndTime);
        int duration = getTaskDuration(task);
        task.setLateStartDate(projectEndTime.minusDays(duration));
      } else {
        for (WorkTaskDependency dep : successors) {
          WorkPlanTask successor = taskMap.get(dep.getPostTaskId());
          if (successor != null && successor.getLateStartDate() != null) {
            LocalDate lateFinish = successor.getLateStartDate().minusDays(dep.getLagDays());
            if (minLateStart == null || lateFinish.isBefore(minLateStart)) {
              minLateStart = lateFinish;
            }
          }
        }

        if (minLateStart != null) {
          task.setLateFinishDate(minLateStart);
          int duration = getTaskDuration(task);
          task.setLateStartDate(minLateStart.minusDays(duration));
        }
      }
    }
  }

  /**
   * 计算浮动时间和关键路径
   */
  private List<WorkPlanTask> calculateFloatAndCriticalPath(List<WorkPlanTask> tasks) {
    List<WorkPlanTask> criticalPath = new ArrayList<>();

    for (WorkPlanTask task : tasks) {
      if (task.getEarlyStartDate() != null && task.getLateStartDate() != null) {
        long floatDays = ChronoUnit.DAYS.between(task.getEarlyStartDate(), task.getLateStartDate());
        task.setFloatDays((int) floatDays);
        task.setIsCriticalPath(floatDays == 0);

        if (floatDays == 0) {
          criticalPath.add(task);
        }
      }
    }

    return criticalPath.stream()
            .sorted(Comparator.comparing(WorkPlanTask::getEarlyStartDate))
            .collect(Collectors.toList());
  }

  /**
   * 拓扑排序 - 新模式：基于前置依赖
   */
  private List<WorkPlanTask> topologicalSort(List<WorkPlanTask> tasks, List<WorkTaskDependency> dependencies) {
    Map<String, List<String>> graph = new HashMap<>();
    Map<String, Integer> inDegree = new HashMap<>();

    // 初始化
    for (WorkPlanTask task : tasks) {
      graph.put(task.getId(), new ArrayList<>());
      inDegree.put(task.getId(), 0);
    }

    // 构建图：从前置任务指向当前任务
    for (WorkTaskDependency dep : dependencies) {
      graph.get(dep.getPrevTaskId()).add(dep.getPostTaskId());
      inDegree.put(dep.getPostTaskId(), inDegree.get(dep.getPostTaskId()) + 1);
    }

    // 拓扑排序
    Queue<String> queue = new LinkedList<>();
    for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.offer(entry.getKey());
      }
    }

    List<WorkPlanTask> result = new ArrayList<>();
    Map<String, WorkPlanTask> taskMap = tasks.stream()
            .collect(Collectors.toMap(WorkPlanTask::getId, task -> task));

    while (!queue.isEmpty()) {
      String taskId = queue.poll();
      result.add(taskMap.get(taskId));

      for (String successor : graph.get(taskId)) {
        inDegree.put(successor, inDegree.get(successor) - 1);
        if (inDegree.get(successor) == 0) {
          queue.offer(successor);
        }
      }
    }

    return result;
  }

  /**
   * 检测循环依赖 - 新模式：基于前置依赖
   */
  private boolean detectCircularDependency(List<WorkTaskDependency> dependencies) {
    Map<String, List<String>> graph = new HashMap<>();
    Set<String> visited = new HashSet<>();
    Set<String> recursionStack = new HashSet<>();

    // 构建图：从前置任务指向当前任务
    for (WorkTaskDependency dep : dependencies) {
      graph.computeIfAbsent(dep.getPrevTaskId(), k -> new ArrayList<>())
              .add(dep.getPostTaskId());
    }

    // DFS检测环
    for (String node : graph.keySet()) {
      if (hasCycle(node, graph, visited, recursionStack)) {
        return true;
      }
    }

    return false;
  }

  private boolean hasCycle(String node,
                           Map<String, List<String>> graph,
                           Set<String> visited,
                           Set<String> recursionStack) {
    if (recursionStack.contains(node)) {
      return true;
    }
    if (visited.contains(node)) {
      return false;
    }

    visited.add(node);
    recursionStack.add(node);

    List<String> neighbors = graph.get(node);
    if (neighbors != null) {
      for (String neighbor : neighbors) {
        if (hasCycle(neighbor, graph, visited, recursionStack)) {
          return true;
        }
      }
    }

    recursionStack.remove(node);
    return false;
  }

  /**
   * 获取任务工期
   */
  private int getTaskDuration(WorkPlanTask task) {
    if (task.getEstimatedDuration() != null && task.getEstimatedDuration() > 0) {
      return task.getEstimatedDuration();
    }
    if (task.getPlanStartDate() != null && task.getPlanEndDate() != null) {
      return (int) ChronoUnit.DAYS.between(task.getPlanStartDate(), task.getPlanEndDate()) + 1;
    }
    return 1; // 默认1天
  }

  /**
   * 保存计算结果
   */
  private void saveCalculationResults(List<WorkPlanTask> tasks) {
    for (WorkPlanTask task : tasks) {
      taskDao.store(task);
    }
  }

  /**
   * 计算风险等级
   */
  private String calculateRiskLevel(PERTAnalysisResult result) {
    if (result.getCriticalTasks() == 0) {
      return "LOW";
    }

    double criticalRatio = (double) result.getCriticalTasks() / result.getTotalTasks();
    if (criticalRatio > 0.7) {
      return "HIGH";
    } else if (criticalRatio > 0.4) {
      return "MEDIUM";
    } else {
      return "LOW";
    }
  }

  /**
   * 初始化任务日期数据
   */
  private void initializeTaskDates(List<WorkPlanTask> tasks) {
    LocalDate defaultStartDate = LocalDate.now();

    for (WorkPlanTask task : tasks) {
      // 初始化最早时间为空，等待计算
      task.setEarlyStartDate(null);
      task.setEarlyFinishDate(null);

      // 初始化最晚时间为空，等待计算
      task.setLateStartDate(null);
      task.setLateFinishDate(null);

      // 初始化浮动时间和关键路径标识
      task.setFloatDays(null);
      task.setIsCriticalPath(false);

      // 确保任务有默认的计划开始日期
      if (task.getPlanStartDate() == null) {
        task.setPlanStartDate(defaultStartDate);
      }
    }
  }

  /**
   * 验证计算结果的有效性
   */
  private boolean validateCalculationResults(List<WorkPlanTask> tasks) {
    for (WorkPlanTask task : tasks) {
      // 检查最早时间是否计算完成
      if (task.getEarlyStartDate() == null || task.getEarlyFinishDate() == null) {
        log.warn("任务 {} 的最早时间计算失败", task.getId());
        return false;
      }

      // 检查最晚时间是否计算完成
      if (task.getLateStartDate() == null || task.getLateFinishDate() == null) {
        log.warn("任务 {} 的最晚时间计算失败", task.getId());
        return false;
      }

      // 检查时间逻辑是否正确
      if (task.getEarlyStartDate().isAfter(task.getEarlyFinishDate())) {
        log.warn("任务 {} 的最早开始时间晚于最早结束时间", task.getId());
        return false;
      }

      if (task.getLateStartDate().isAfter(task.getLateFinishDate())) {
        log.warn("任务 {} 的最晚开始时间晚于最晚结束时间", task.getId());
        return false;
      }

      // 检查浮动时间是否非负数
      if (task.getFloatDays() != null && task.getFloatDays() < 0) {
        log.warn("任务 {} 的浮动时间为负数: {}", task.getId(), task.getFloatDays());
        return false;
      }
    }

    return true;
  }

  public void convertData(WorkTaskDependency entity) {
    if (null == entity)
      return;
    String prevId = entity.getPrevTaskId();
    String postId = entity.getPostTaskId();

    List<String> taskIdList = new ArrayList<>();
    if (StringUtils.isNotBlank(prevId))
      taskIdList.add(prevId);
    if (StringUtils.isNotBlank(postId))
      taskIdList.add(postId);

    if (CollectionUtils.isNotEmpty(taskIdList)) {
      Map<String, WorkPlanTask> taskMap = taskDao.findMapInId(taskIdList, WorkPlanTask.class);
      getTaskName(entity, taskMap);
    }
  }

  public void convertCollection(List<WorkTaskDependency> list) {
    List<String> taskIdList = new ArrayList<>();

    List<String> prevTaskIdList = list.stream()
            .map(WorkTaskDependency::getPrevTaskId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    List<String> postTaskIdList = list.stream()
            .map(WorkTaskDependency::getPostTaskId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    taskIdList.addAll(prevTaskIdList);
    taskIdList.addAll(postTaskIdList);

    if (CollectionUtils.isNotEmpty(taskIdList)) {
      taskIdList = new ArrayList<>(new HashSet<>(taskIdList));

      Map<String, WorkPlanTask> taskMap = taskDao.findMapInId(taskIdList, WorkPlanTask.class);

      list.forEach(dependency -> {
        getTaskName(dependency, taskMap);
      });

    }

  }

  private void getTaskName(WorkTaskDependency entity, Map<String, WorkPlanTask> taskMap) {

    if (null == entity)
      return;
    String prevId = entity.getPrevTaskId();
    String postId = entity.getPostTaskId();
    if (StringUtils.isNotBlank(prevId)) {
      WorkPlanTask prevTask = MapUtils.getObject(taskMap, prevId, null);
      entity.setPrevTaskName(null == prevTask ? "" : prevTask.getTaskName());
    }
    if (StringUtils.isNotBlank(postId)) {
      WorkPlanTask postTask = MapUtils.getObject(taskMap, postId, null);
      entity.setPostTaskName(null == postTask ? "" : postTask.getTaskName());
    }
  }

  public boolean validate(@Valid WorkTaskDependency entity) {
    String dependencyCode = entity.getDependencyCode();

    if (StringUtils.isBlank(dependencyCode))
      throw new BizException("Code is null");

    String reg = "^[a-zA-Z0-9_]+$";
    if (!dependencyCode.matches(reg))
      throw new BizException("Code is invalid");

    String prevTaskId = entity.getPrevTaskId();
    String postTaskId = entity.getPostTaskId();
    if(StringUtils.isBlank(prevTaskId) && StringUtils.isBlank(postTaskId))
      throw new BizException("前置任务和后置任务不能同时为空");

    if(StringUtils.isNotBlank(prevTaskId) ){
      if (!taskDao.existsById(prevTaskId))
        throw new BizException("紧前任务不存在");
    }
    if(StringUtils.isNotBlank(postTaskId) ){
      if (!taskDao.existsById(postTaskId))
        throw new BizException("紧后任务不存在");
    }

    if(StringUtils.isNotBlank(prevTaskId) && StringUtils.isNotBlank(postTaskId)){
      if (prevTaskId.equals(postTaskId))
        throw new BizException("任务不能依赖自己");
    }

    return true;
  }

}
