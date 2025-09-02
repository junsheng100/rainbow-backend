package com.rainbow.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_post")
@org.hibernate.annotations.Table(appliesTo = "user_post", comment = "用户-岗位")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcd","fcu","lcd","lcu"})
public class UserPost extends BaseEntity {

  @Id
  @Schema(title = "ID", type = "Long")
  @Column(length = 20)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @UnionKey
  @Schema(title = "用户ID", type = "String")
  @Column(length = 36)
  private String userId;

  @UnionKey
  @Column(length = 20)
  @Schema(title = "岗位序号", type = "Long")
  private Long postId;

  @Transient
  @Schema(title = "岗位名称", type = "String")
  private String postName;

  public UserPost(){

  }

  public UserPost(String userId,Long postId){
    this.userId = userId;
    this.postId = postId;
  }
  public UserPost(String userId, Long postId, String fcu, LocalDateTime fcd, String lcu, LocalDateTime lcd, String status){
    super(fcu, fcd, lcu, lcd, status);
    this.userId = userId;
    this.postId = postId;
  }
}
