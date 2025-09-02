package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sys_user_passwd")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class UserPasswd extends BaseEntity {


  @Id
  @Schema(title = "ID",type = "Long")
  @Column(length = 20)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @UnionKey
  @Schema(title = "用户ID", type = "String")
  @Column(length = 36,unique = true)
  private String userId;


  @Schema(title = "密码", type = "String")
  @Column
  private String password;

  public UserPasswd() {
  }

  public UserPasswd(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }

}
