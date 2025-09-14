package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.entity
 * @Filename：UserStyle
 * @Describe:
 */
@Getter
@Setter
@Entity
@Table(name = "user_style")
@org.hibernate.annotations.Table(appliesTo = "user_style", comment = "用户-界面设置")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class UserStyle extends BaseEntity {


  @Id
  @Column(length = 20)
  @Schema(title = "菜单ID", type = "Long")
  private String id;


  @Schema(title = "用户ID", type = "String")
  @Column(length = 36)
  private String userId;




}
