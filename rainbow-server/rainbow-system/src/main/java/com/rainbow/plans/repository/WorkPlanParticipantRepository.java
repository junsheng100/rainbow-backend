package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkPlanParticipant;
import com.rainbow.plans.enums.ParticipantRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 计划参与者数据访问接口
 *
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanParticipantRepository extends BaseRepository<WorkPlanParticipant, String> {

  /**
   * 根据计划ID查询参与者
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.status = '0' ORDER BY wp.joinDate ASC")
  List<WorkPlanParticipant> findByPlanId(@Param("planId") String planId);

  /**
   * 根据用户ID查询参与的计划
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.userId = :userId AND wp.status = '0' ORDER BY wp.joinDate DESC")
  List<WorkPlanParticipant> findByUserId(@Param("userId") String userId);

  /**
   * 根据角色查询参与者
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.role = :role AND wp.status = '0' ORDER BY wp.joinDate DESC")
  List<WorkPlanParticipant> findByRole(@Param("role") ParticipantRole role);

  /**
   * 根据计划ID和用户ID查询参与者
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.userId = :userId AND wp.status = '0'")
  WorkPlanParticipant findByPlanIdAndUserId(@Param("planId") String planId, @Param("userId") String userId);

  /**
   * 根据计划ID和角色查询参与者
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.role = :role AND wp.status = '0' ORDER BY wp.joinDate ASC")
  List<WorkPlanParticipant> findByPlanIdAndRole(@Param("planId") String planId, @Param("role") ParticipantRole role);

  /**
   * 检查用户是否为计划参与者
   */
  @Query("SELECT COUNT(wp) > 0 FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.userId = :userId AND wp.status = '0'")
  boolean existsByPlanIdAndUserId(@Param("planId") String planId, @Param("userId") String userId);

  /**
   * 统计计划参与者数量
   */
  @Query("SELECT COUNT(wp) FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.status = '0'")
  long countByPlanId(@Param("planId") String planId);

  /**
   * 根据角色统计参与者数量
   */
  @Query("SELECT COUNT(wp) FROM WorkPlanParticipant wp WHERE wp.role = :role AND wp.status = '0'")
  long countByRole(@Param("role") ParticipantRole role);

  /**
   * 查询用户参与的计划数量
   */
  @Query("SELECT COUNT(wp) FROM WorkPlanParticipant wp WHERE wp.userId = :userId AND wp.status = '0'")
  long countByUserId(@Param("userId") String userId);

  /**
   * 查询计划的所有创建者
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.role = 'CREATOR' AND wp.status = '0'")
  List<WorkPlanParticipant> findCreatorsByPlanId(@Param("planId") String planId);

  /**
   * 查询计划的所有负责人
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.role = 'OWNER' AND wp.status = '0'")
  List<WorkPlanParticipant> findOwnersByPlanId(@Param("planId") String planId);

  /**
   * 查询计划的所有成员
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.role = 'MEMBER' AND wp.status = '0'")
  List<WorkPlanParticipant> findMembersByPlanId(@Param("planId") String planId);

  /**
   * 查询计划的所有观察者
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.planId = :planId AND wp.role = 'OBSERVER' AND wp.status = '0'")
  List<WorkPlanParticipant> findObserversByPlanId(@Param("planId") String planId);

  /**
   * 查询用户有编辑权限的计划
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.userId = :userId AND wp.role IN ('CREATOR', 'OWNER') AND wp.status = '0'")
  List<WorkPlanParticipant> findEditablePlansByUserId(@Param("userId") String userId);

  /**
   * 查询用户有查看权限的计划
   */
  @Query("SELECT wp FROM WorkPlanParticipant wp WHERE wp.userId = :userId AND wp.status = '0'")
  List<WorkPlanParticipant> findViewablePlansByUserId(@Param("userId") String userId);

  /**
   * 创建计划
   */
  @Query("select wp from WorkPlanParticipant wp where wp.planId = ?1 and wp.role = ?2 ")
  WorkPlanParticipant findCreateByPlanId(String planId, ParticipantRole code);

  @Query("select t.userId from WorkPlanParticipant t where t.planId = ?1 ")
  List<String> findUserIdByPlanId(String planId);

  @Query("select t from WorkPlanParticipant t where t.planId in (?1) ")
  List<WorkPlanParticipant> findInPlanId(List<String> plandIdList);

  @Modifying
  @Query("delete from WorkPlanParticipant where planId = ?1 ")
  void deleteByPlanId(String planId);

  @Modifying
  @Query("delete from WorkPlanParticipant where planId = ?1 and role = ?2 ")
  void deleteByPlanIdAndRole(String planId, ParticipantRole participantRole);

}
