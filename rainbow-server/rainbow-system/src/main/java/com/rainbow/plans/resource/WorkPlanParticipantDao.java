package com.rainbow.plans.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.plans.entity.WorkPlanParticipant;
import com.rainbow.plans.enums.ParticipantRole;

import java.util.List;

/**
 * 计划参与者数据访问接口
 *
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanParticipantDao extends BaseDao<WorkPlanParticipant, String> {

  /**
   * 根据计划ID查询参与者
   */
  List<WorkPlanParticipant> findByPlanId(String planId);

  /**
   * 根据用户ID查询参与的计划
   */
  List<WorkPlanParticipant> findByUserId(String userId);

  /**
   * 根据角色查询参与者
   */
  List<WorkPlanParticipant> findByRole(ParticipantRole role);

  /**
   * 根据计划ID和用户ID查询参与者
   */
  WorkPlanParticipant findByPlanIdAndUserId(String planId, String userId);

  /**
   * 根据计划ID和角色查询参与者
   */
  List<WorkPlanParticipant> findByPlanIdAndRole(String planId, ParticipantRole role);

  /**
   * 检查用户是否为计划参与者
   */
  boolean existsByPlanIdAndUserId(String planId, String userId);

  /**
   * 统计计划参与者数量
   */
  long countByPlanId(String planId);

  /**
   * 删除计划的所有参与者
   */
  void deleteByPlanId(String planId);

  WorkPlanParticipant findCreateByPlanId(String planId);

  void updateByPlanIdAndRole(String planId, List<WorkPlanParticipant> participants);

  List<String> findUserIdByPlanId(String planId);

  void convertCollection(List<WorkPlanParticipant> participantList);

  List<WorkPlanParticipant> findInPlanId(List<String> plandIdList);

  void deleteByPlanIdAndRole(java.lang.String planId, ParticipantRole participantRole);
}
