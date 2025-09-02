package com.rainbow.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.monitor.model.server.Sys;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "server_sys_data")
@org.hibernate.annotations.Table(appliesTo = "server_sys_data", comment = "server 数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class SysData extends BaseEntity {


  @Id
  @Column(length = 20)
  @Schema(title = "菜单ID", type = "Long")
  private String Id;

  @UnionKey
  @Column( columnDefinition = "varchar(50) comment '服务器名称'")
  private String computerName;

  @UnionKey
  @Column(columnDefinition = "varchar(50) comment '服务器Ip'")
  private String computerIp;

  @Column( columnDefinition = "varchar(50) comment '操作系统'")
  private String osName;

  @Column(columnDefinition = "varchar(50) comment '系统架构'")
  private String osArch;

  @Transient
  private CpuData cpuData;
  @Transient
  private JvmData jvmData;
  @Transient
  private MemData memData;
  @Transient
  private List<DiskData> diskData;



  public SysData() {
  }


  public SysData(Sys sys) {
    this.computerIp = sys.getComputerIp();
    this.computerName = sys.getComputerName();
    this.osName = sys.getOsName();
    this.osArch = sys.getOsArch();
  }

  public SysData(CpuData cpuData,  JvmData jvmData, MemData memData, List<DiskData> diskData) {
    this.cpuData = cpuData;
    this.jvmData = jvmData;
    this.memData = memData;
    this.diskData = diskData;
  }


}
