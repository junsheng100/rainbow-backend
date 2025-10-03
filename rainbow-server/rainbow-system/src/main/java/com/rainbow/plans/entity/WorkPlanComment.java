package com.rainbow.plans.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.Keyword;
import com.rainbow.base.annotation.Search;
import com.rainbow.base.annotation.SearchFilter;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.enums.SearchEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 计划评论实体
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_plan_comment")
@org.hibernate.annotations.Table(appliesTo = "work_plan_comment", comment = "计划评论表")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class WorkPlanComment extends BaseEntity {

    @Id
    @Column(length = 20)
    @Schema(title = "评论ID", type = "String")
    private String id;

    @NotBlank(message = "计划ID不能为空")
    @Column(name = "plan_id", length = 20)
    @Schema(title = "计划ID", type = "String")
    private String planId;

    @Column(name = "task_id", length = 20)
    @Schema(title = "任务ID", type = "String")
    private String taskId;

    @Column(name = "parent_id", length = 20)
    @Schema(title = "父评论ID", type = "String")
    private String parentId;

    @NotBlank(message = "评论内容不能为空")
    @Search(SELECT = SearchEnum.LIKE)
    @Column(columnDefinition = "TEXT")
    @Schema(title = "评论内容", type = "String")
    private String content;

    @Column(name = "comment_type", length = 20)
    @Schema(title = "评论类型", type = "String")
    private String commentType;

    @NotBlank(message = "评论人ID不能为空")
    @Column(name = "author_id", length = 36)
    @Schema(title = "评论人ID", type = "String")
    private String authorId;

    @Column(name = "is_important")
    @Schema(title = "是否重要", type = "Boolean")
    private Boolean isImportant;

    // 关联字段
    @Transient
    @Schema(title = "评论人姓名", type = "String")
    private String authorName;

    @Transient
    @Schema(title = "评论人头像", type = "String")
    private String authorAvatar;

    @Transient
    @Schema(title = "子评论列表", type = "List")
    private java.util.List<WorkPlanComment> children;

    public WorkPlanComment() {
        this.commentType = "COMMENT";
        this.isImportant = false;
    }
}
