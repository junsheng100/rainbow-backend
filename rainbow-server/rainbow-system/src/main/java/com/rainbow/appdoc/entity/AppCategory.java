package com.rainbow.appdoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;

/**
 * 控制类管理实体
 */
@Data
@Entity
@Table(name = "app_category")
@org.hibernate.annotations.Table(appliesTo = "app_category", comment = "接口类")
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class AppCategory extends BaseEntity {

    /** 主键ID */
    @Id
    @Column(length = 20)
    private String id;

    /** 控制类名称 */

    @Column(nullable = false, length = 128,unique = true)
    private String className;

    @UnionKey
    @Column(nullable = false, length = 64)
    private String simpleName;

    @Column(nullable = false, length = 128)
    private String requestUrl;

    /** 描述信息 */
    @OrderBy(value = Sort.Direction.ASC,INDEX = "999")
    @Column(length = 256)
    private String description;

    /** 启用:0,禁用:1 */
    @Column(columnDefinition = "int(1) default 0 ")
    private Integer disabled;


    @Schema(title = "排序号", type = "Integer")
    @Column(length = 11)
    private Integer orderNum;

    @Transient
    private List<AppInterface> interfaces;


} 