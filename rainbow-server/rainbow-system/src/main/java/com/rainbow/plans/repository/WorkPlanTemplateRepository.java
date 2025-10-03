package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkPlanTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 计划模板数据访问接口
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanTemplateRepository extends BaseRepository<WorkPlanTemplate, String> {

    /**
     * 根据模板类型查询模板
     */
    @Query("SELECT wt FROM WorkPlanTemplate wt WHERE wt.templateType = :templateType AND wt.status = '0' ORDER BY wt.usageCount DESC, wt.fcd DESC")
    List<WorkPlanTemplate> findByTemplateType(@Param("templateType") String templateType);

    /**
     * 根据创建者ID查询模板
     */
    @Query("SELECT wt FROM WorkPlanTemplate wt WHERE wt.creatorId = :creatorId AND wt.status = '0' ORDER BY wt.fcd DESC")
    List<WorkPlanTemplate> findByCreatorId(@Param("creatorId") String creatorId);

    /**
     * 查询公开模板
     */
    @Query("SELECT wt FROM WorkPlanTemplate wt WHERE wt.isPublic = true AND wt.status = '0' ORDER BY wt.usageCount DESC, wt.fcd DESC")
    List<WorkPlanTemplate> findPublicTemplates();

    /**
     * 根据模板名称模糊查询
     */
    @Query("SELECT wt FROM WorkPlanTemplate wt WHERE wt.templateName LIKE %:templateName% AND wt.status = '0' ORDER BY wt.usageCount DESC, wt.fcd DESC")
    List<WorkPlanTemplate> findByTemplateNameLike(@Param("templateName") String templateName);

    /**
     * 查询热门模板（使用次数最多）
     */
    @Query("SELECT wt FROM WorkPlanTemplate wt WHERE wt.status = '0' ORDER BY wt.usageCount DESC, wt.fcd DESC")
    List<WorkPlanTemplate> findPopularTemplates(@Param("limit") int limit);

    /**
     * 查询最新模板
     */
    @Query("SELECT wt FROM WorkPlanTemplate wt WHERE wt.status = '0' ORDER BY wt.fcd DESC")
    List<WorkPlanTemplate> findLatestTemplates(@Param("limit") int limit);

    /**
     * 统计模板数量
     */
    @Query("SELECT COUNT(wt) FROM WorkPlanTemplate wt WHERE wt.status = '0'")
    long countAll();

    /**
     * 统计公开模板数量
     */
    @Query("SELECT COUNT(wt) FROM WorkPlanTemplate wt WHERE wt.isPublic = true AND wt.status = '0'")
    long countPublicTemplates();

    /**
     * 统计用户模板数量
     */
    @Query("SELECT COUNT(wt) FROM WorkPlanTemplate wt WHERE wt.creatorId = :creatorId AND wt.status = '0'")
    long countByCreatorId(@Param("creatorId") String creatorId);

    /**
     * 根据模板类型统计数量
     */
    @Query("SELECT COUNT(wt) FROM WorkPlanTemplate wt WHERE wt.templateType = :templateType AND wt.status = '0'")
    long countByTemplateType(@Param("templateType") String templateType);

    /**
     * 查询模板使用统计
     */
    @Query("SELECT wt.templateType, SUM(wt.usageCount) FROM WorkPlanTemplate wt WHERE wt.status = '0' GROUP BY wt.templateType")
    List<Object[]> getTemplateUsageStatistics();

    /**
     * 查询用户创建的模板统计
     */
    @Query("SELECT wt.creatorId, COUNT(wt) FROM WorkPlanTemplate wt WHERE wt.status = '0' GROUP BY wt.creatorId")
    List<Object[]> getCreatorTemplateStatistics();
}
