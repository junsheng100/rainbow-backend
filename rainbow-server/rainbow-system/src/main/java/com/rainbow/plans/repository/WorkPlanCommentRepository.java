package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkPlanComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 计划评论数据访问接口
 * 
 * @author rainbow
 * @since 2024-01-01
 */
public interface WorkPlanCommentRepository extends BaseRepository<WorkPlanComment, String> {

    /**
     * 根据计划ID查询评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.planId = :planId AND wc.status = '0' ORDER BY wc.fcd ASC")
    List<WorkPlanComment> findByPlanId(@Param("planId") String planId);

    /**
     * 根据任务ID查询评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.taskId = :taskId AND wc.status = '0' ORDER BY wc.fcd ASC")
    List<WorkPlanComment> findByTaskId(@Param("taskId") String taskId);

    /**
     * 根据父评论ID查询子评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.parentId = :parentId AND wc.status = '0' ORDER BY wc.fcd ASC")
    List<WorkPlanComment> findByParentId(@Param("parentId") String parentId);

    /**
     * 根据评论人ID查询评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.authorId = :authorId AND wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findByAuthorId(@Param("authorId") String authorId);

    /**
     * 根据评论类型查询评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.commentType = :commentType AND wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findByCommentType(@Param("commentType") String commentType);

    /**
     * 查询重要评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.isImportant = true AND wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findImportantComments();

    /**
     * 根据计划ID查询重要评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.planId = :planId AND wc.isImportant = true AND wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findImportantCommentsByPlanId(@Param("planId") String planId);

    /**
     * 根据任务ID查询重要评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.taskId = :taskId AND wc.isImportant = true AND wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findImportantCommentsByTaskId(@Param("taskId") String taskId);

    /**
     * 统计计划评论数量
     */
    @Query("SELECT COUNT(wc) FROM WorkPlanComment wc WHERE wc.planId = :planId AND wc.status = '0'")
    long countByPlanId(@Param("planId") String planId);

    /**
     * 统计任务评论数量
     */
    @Query("SELECT COUNT(wc) FROM WorkPlanComment wc WHERE wc.taskId = :taskId AND wc.status = '0'")
    long countByTaskId(@Param("taskId") String taskId);

    /**
     * 统计用户评论数量
     */
    @Query("SELECT COUNT(wc) FROM WorkPlanComment wc WHERE wc.authorId = :authorId AND wc.status = '0'")
    long countByAuthorId(@Param("authorId") String authorId);

    /**
     * 根据内容模糊查询评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.content LIKE %:content% AND wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findByContentLike(@Param("content") String content);

    /**
     * 查询最新的评论
     */
    @Query("SELECT wc FROM WorkPlanComment wc WHERE wc.status = '0' ORDER BY wc.fcd DESC")
    List<WorkPlanComment> findLatestComments(@Param("limit") int limit);

    /**
     * 根据计划ID查询最新评论
     */
    @Query(value = "SELECT * FROM work_plan_comment wc WHERE wc.plan_id = :planId AND wc.status = '0' ORDER BY wc.fcd DESC LIMIT :limit", nativeQuery = true)
    List<WorkPlanComment> findLatestCommentsByPlanId(@Param("planId") String planId, @Param("limit") int limit);
}
