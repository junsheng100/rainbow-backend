package com.rainbow.monitor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerEntity extends BaseEntity {

  @Id
  @Column(length = 20)
  @Schema(title = "ID", type = "String")
  private String Id;

  @Column(length = 20)
  @Schema(title = "Sys ID", type = "String")
  private String sysId;

}
