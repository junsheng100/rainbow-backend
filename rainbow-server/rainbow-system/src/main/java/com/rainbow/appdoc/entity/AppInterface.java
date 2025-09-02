package com.rainbow.appdoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 接口管理实体
 */
@Data
@Entity
@Table(name = "app_interface")
@org.hibernate.annotations.Table(appliesTo = "app_interface", comment = "接口方法")
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class AppInterface extends BaseEntity {

    /** 主键ID */
    @Id
    @Column(length = 20)
    private String id;

    /** 关联控制类ID */
    @UnionKey
    @NotNull(message = "类 ID 不能为空")
    @Column(nullable = false)
    private String categoryId;

    /** 方法命名 */
    @UnionKey
    @NotNull(message = " 方法命名 不能为空")
    @Column(length = 256)
    private String methodName;

    /** 请求URL */
    @Column(length = 256)
    private String requestUrl;

    /** 请求方法（GET、POST等） */
    @Column(length = 16)
    private String requestMethod;
    
    @Column(length = 512)
    private String headers;

    @Column
    private String consumes;

    @Column
    private String produces;

    /** 接口描述 */
    @Column(length = 256)
    private String description;

    /** 启用:0,禁用:1 */
    @Column(columnDefinition = "int(1) default 0 ")
    private Integer disabled;


    /** 校验 否:0,是:1 */
    @Column(columnDefinition = "int(1) default 0 ")
    private Integer  valid;

    /** 客户端类型 逗号分割 */
    @Column(length = 128)
    private String clientTypes;


    @OrderBy(value = Sort.Direction.ASC,INDEX = "0")
    @Column(length = 11)
    private Integer orderNum;

} 