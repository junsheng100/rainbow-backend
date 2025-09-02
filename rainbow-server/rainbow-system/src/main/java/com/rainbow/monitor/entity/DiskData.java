package com.rainbow.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.monitor.model.entity.ServerDataEntity;
import com.rainbow.monitor.model.server.SysFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "server_disk_data")
@org.hibernate.annotations.Table(appliesTo = "server_disk_data", comment = "server File 数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu", "lcd", "lcu"})
public class DiskData extends ServerDataEntity {


  @Column(columnDefinition = "varchar(255) COMMENT '盘符路径'")
  private String dirName;

  @Column(columnDefinition = "varchar(255) COMMENT '盘符类型'")
  private String sysTypeName;

  @Column(columnDefinition = "varchar(255) COMMENT '文件类型'")
  private String typeName;

  @Column(name = "disk_total", columnDefinition = "varchar(255) COMMENT '总大小'")
  private String total;

  @Column(name = "disk_free", columnDefinition = "varchar(255) COMMENT '剩余大小'")
  private String free;

  @Column(name = "disk_used", columnDefinition = "varchar(255) COMMENT '已经使用量'")
  private String used;

  @Column(name = "disk_usage", columnDefinition = "double(10,2) COMMENT '资源的使用率'")
  private Double usage;


  public DiskData() {
    super();
  }

  public DiskData(SysFile data) {
    super();
    this.dirName = data.getDirName();
    this.sysTypeName = data.getSysTypeName();
    this.typeName = data.getTypeName();
    this.total = data.getTotal();
    this.free = data.getFree();
    this.used = data.getUsed();
    this.usage = data.getUsage();
  }
}
