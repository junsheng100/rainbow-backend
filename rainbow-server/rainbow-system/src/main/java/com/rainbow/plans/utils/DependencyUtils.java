package com.rainbow.plans.utils;

import com.rainbow.plans.entity.WorkTaskDependency;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.utils
 * @Filename：DependencyUtils
 * @Date：2025/9/27 18:23
 * @Describe:
 */
public class DependencyUtils {

  public static boolean hasCycle(DependencyNode root) {

    if (null == root || null == root.getNext())
      return false;

    DependencyNode slow = root;
    DependencyNode fast = root.getNext();

    while (fast != null && fast.getNext() != null) {
      if (slow.getVal().equals(fast.getVal())) {
        return true;
      }
      slow = slow.getNext();
      fast = fast.getNext().getNext();
    }

    return false;
  }

  public static boolean hasCycle(List<DependencyNode> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      for (DependencyNode node : list) {
        if (hasCycle(node)) {
          return true;
        }
      }
    }
    return false;
  }


  public static boolean compareSameTask(WorkTaskDependency a, WorkTaskDependency d) {
    if (null == a || null == d)
      return false;
    return a.getPrevTaskId().equals(d.getPrevTaskId()) && a.getPostTaskId().equals(d.getPostTaskId());
  }


  /**
   * 判断两个任务是否是循环依赖
   * @param current
   * @param next
   * @return
   */
  public static boolean isLoopDependencyTask(WorkTaskDependency current, WorkTaskDependency next) {
    if (null == current || null == next)
      return false;

    String aPrevTaskId = current.getPrevTaskId();
    String aPostTaskId = current.getPostTaskId();
    String dPrevTaskId = next.getPrevTaskId();
    String dPostTaskId = next.getPostTaskId();

    // 全为空是异常
    if (null == aPrevTaskId  && null == aPostTaskId
            && null == dPrevTaskId && null == dPostTaskId)
      throw new RuntimeException("请检查数据 的完整性");
    // 全不为空的做交互比较
    if (null != aPrevTaskId  && null != aPostTaskId
            && null != dPrevTaskId && null != dPostTaskId)
      return aPrevTaskId.equals(dPostTaskId) && aPostTaskId.equals(dPrevTaskId);
    // A 前置为空,B 后置为空 做交互比较
    if (null == aPrevTaskId  && null != aPostTaskId
            && null != dPrevTaskId && null == dPostTaskId)
      return aPostTaskId.equals(dPrevTaskId);
    // B 前置为空,A 后置为空 做交互比较
    if (null != aPrevTaskId  && null == aPostTaskId
            && null == dPrevTaskId && null != dPostTaskId)
      return aPrevTaskId.equals(dPostTaskId);

    return false;
  }
  public static boolean isExists(WorkTaskDependency current, WorkTaskDependency next) {
    if (null == current || null == next)
      return false;

    String aPrevTaskId = current.getPrevTaskId();
    String aPostTaskId = current.getPostTaskId();
    String dPrevTaskId = next.getPrevTaskId();
    String dPostTaskId = next.getPostTaskId();

    // 全为空是异常
    if (null == aPrevTaskId  && null == aPostTaskId
            && null == dPrevTaskId && null == dPostTaskId)
      throw new RuntimeException("请检查数据 的完整性");

    // 全不为空的做交互比较
    if (null != aPrevTaskId  && null != aPostTaskId
            && null != dPrevTaskId && null != dPostTaskId)
      return aPrevTaskId.equals(dPrevTaskId) && aPostTaskId.equals(dPostTaskId);

    // A 前置为空,B 后置为空 做交互比较
    if (null == aPrevTaskId  && null != aPostTaskId
            && null == dPrevTaskId && null != dPostTaskId)
      return aPostTaskId.equals(dPostTaskId);


    return false;
  }

  /**
   * 判断任务列表是否是循环依赖 A->B && B->A
   * @param list
   * @return
   */
  public static boolean isLoopDependencyTask(List<WorkTaskDependency> list) {
    if (CollectionUtils.isEmpty(list))
      return false;

    for(WorkTaskDependency current:list){
     Long count = list.stream().filter(next ->isLoopDependencyTask(current,next)).count();
     if(count >0){
       return true;
     }
    }

    return false;
  }

}
