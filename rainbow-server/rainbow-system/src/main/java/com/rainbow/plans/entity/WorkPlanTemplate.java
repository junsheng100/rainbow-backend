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
 * 计划模板实体
 * 
 * @author rainbow
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "work_plan_template")
@org.hibernate.annotations.Table(appliesTo = "work_plan_template", comment = "计划模板表")
@SearchFilter(@Keyword(key = "status", value = "0"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class WorkPlanTemplate extends BaseEntity {

    @Id
    @Column(length = 20)
    @Schema(title = "模板ID", type = "String")
    private String id;

    @NotBlank(message = "模板名称不能为空")
    @Search(SELECT = SearchEnum.LIKE)
    @Column(name = "template_name", length = 200)
    @Schema(title = "模板名称", type = "String")
    private String templateName;

    @Column(columnDefinition = "TEXT")
    @Schema(title = "模板描述", type = "String")
    private String templateDesc;

    @NotBlank(message = "模板类型不能为空")
    @Column(name = "template_type", length = 20)
    @Schema(title = "模板类型", type = "String")
    private String templateType;

    @Column(name = "template_data", columnDefinition = "JSON")
    @Schema(title = "模板数据", type = "String")
    private String templateData;

    @Column(name = "is_public")
    @Schema(title = "是否公开", type = "Boolean")
    private Boolean isPublic;

    @Column(name = "usage_count")
    @Schema(title = "使用次数", type = "Integer")
    private Integer usageCount;

    @NotBlank(message = "创建者ID不能为空")
    @Column(name = "creator_id", length = 36)
    @Schema(title = "创建者ID", type = "String")
    private String creatorId;

    // 关联字段
    @Transient
    @Schema(title = "创建者姓名", type = "String")
    private String creatorName;


}
