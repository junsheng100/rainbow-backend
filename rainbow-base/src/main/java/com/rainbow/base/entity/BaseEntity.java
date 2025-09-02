package com.rainbow.base.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public  class BaseEntity implements Serializable {

  @Schema(title = "首次创建",type = "String")
  @Column(length = 32)
  private String fcu;


  @OrderBy(value = Sort.Direction.DESC,INDEX = "10000")
  @Schema(title = "首次创建时间",type = "LocalDateTime")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime fcd;


  @Schema(title = "最后修改",type = "String")
  @Column(length = 32)
  private String lcu;

  @OrderBy(value = Sort.Direction.DESC,INDEX = "9999")
  @Schema(title = "最后更新时间",type = "LocalDateTime")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime lcd;

  @Column(length = 2)
  @Schema(title = "状态（0:可用,-1:删除）",type = "String")
  private String status;


  public BaseEntity(){

  }

  public BaseEntity( String status){
    this.status = status;
  }
  public BaseEntity(String fcu,LocalDateTime fcd,String lcu,LocalDateTime lcd,String status){
    this.fcu = fcu;
    this.fcd = fcd;
    this.lcu = lcu;
    this.lcd = lcd;
    this.status = status;
  }

}