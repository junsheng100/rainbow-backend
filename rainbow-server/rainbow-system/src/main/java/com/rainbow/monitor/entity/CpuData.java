package com.rainbow.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.monitor.model.entity.ServerDataEntity;
import com.rainbow.monitor.model.server.Cpu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@Table(name = "server_cpu_data")
@org.hibernate.annotations.Table(appliesTo = "server_cpu_data", comment = "server 数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class CpuData extends ServerDataEntity {

  @Schema(title = "核心数", type = "Integer")
  @Column(name = "cpu_num")
  private Integer cpuNum;

  @Schema(title = "CPU总的使用率", type = "Double")
  @Column(name = "cpu_total")
  private Double total;

  @Schema(title = "CPU系统使用率", type = "Double")
  @Column(name = "cpu_sys")
  private Double sys;

  @Schema(title = "CPU用户使用率", type = "Double")
  @Column(name = "cpu_user")
  private Double used;

  @Schema(title = "CPU当前等待率", type = "Double")
  @Column(name = "cpu_wait")
  private Double wait;

  @Schema(title = "CPU当前空闲率", type = "Double")
  @Column(name = "cpu_free")
  private Double free;


  public CpuData(){
    super();
  }

  public CpuData(Cpu cpu){
    super();
    this.cpuNum = cpu.getCpuNum();
    this.total = cpu.getTotal();
    this.sys = cpu.getSys();
    this.used = cpu.getUsed();
    this.wait = cpu.getWait();
    this.free = cpu.getFree();
  }

}
