package com.rainbow.appdoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.OrderBy;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.appInfo.entity

 * @name：ApiParamterType
 * @Date：2025/7/26 16:53
 * @Filename：ApiParamterType
 */

@Data
@Entity
@Table(name = "app_model_type")
@org.hibernate.annotations.Table(appliesTo = "app_model_type", comment = "数据模型")
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class AppModelType extends BaseEntity {
  /** 主键ID */
  @Id
  @Column(length = 20)
  private String id;

  /** 数据格式（JSON格式） */
  @Column(columnDefinition = "longtext")
  private String data;

  /** 数据类型 */
  @Column(length = 12)
  private String type;

  /** 数据类型 */
  @Column(length = 12)
  private String typeName;

  /** MD5校验码 */
  @UnionKey
  @Column(length = 36,unique = true)
  private String md5Code;

  /** 排序号 */
  @OrderBy(value = Sort.Direction.ASC,INDEX = "0")
  @Schema(title = "排序号", type = "Integer")
  @Column(length = 11)
  private Integer orderNum;

}
