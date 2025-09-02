package com.rainbow.appdoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.entity

 * @name：ApiReferenceData
 * @Date：2025/7/26 17:17
 * @Filename：ApiReferenceData
 */

@Data
@Entity
@Table(name = "app_reference")
@org.hibernate.annotations.Table(appliesTo = "app_reference", comment = "数据模型")
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class AppReference extends BaseEntity {

  /** 主键ID */
  @Id
  @Column(length = 20)
  private String id;

  /** 关联接口ID */
  @UnionKey
  @Column(nullable = false)
  private String interfaceId;

  @UnionKey
  @Column(nullable = false)
  private String modelTypeId;


  /** 枚举 DataTypeEnums 0:出参,1:入参*/
  @Column(length = 1)
  private String dataType;

  /** 数据格式（JSON格式） */
  @Transient
  private String data;

  @Transient
  private String md5Code;

  @Column(columnDefinition = "int(1) default 0 ")
  private Boolean disabled;

  @OrderBy(value = Sort.Direction.ASC,INDEX = "0")
  @Column(length = 11)
  private Integer orderNum;



}
