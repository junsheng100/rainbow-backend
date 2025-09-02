package com.rainbow.monitor.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.OrderBy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.monitor.model.entity
 * @Filename：ServerDataEntity
 * @Describe:
 */
@Data
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerDataEntity extends ServerEntity{

  @OrderBy(value = Sort.Direction.DESC)
  @Schema(title = "获取时间",type = "Date")
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date takeTime;

}
