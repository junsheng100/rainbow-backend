package com.rainbow.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.monitor.model.entity.ServerDataEntity;
import com.rainbow.monitor.model.server.Jvm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "server_jvm_data")
@org.hibernate.annotations.Table(appliesTo = "server_jvm_data", comment = "server Java virtual machine 数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu","lcd","lcu"})
public class JvmData extends ServerDataEntity {

  /**
   * 当前JVM占用的内存总数(M)
   */
  @Column(name = "jvm_total",columnDefinition = "double(10,2) comment '当前JVM占用的内存总数(M)'")
  private Double total;

  /**
   * JVM最大可用内存总数(M)
   */
  @Column(name = "jvm_max", columnDefinition = "double(10,2) comment 'JVM最大可用内存总数(M)'")
  private Double max;

  /**
   * JVM空闲内存(M)
   */
  @Column(name = "jvm_free", columnDefinition = "double(10,2) comment 'JVM空闲内存(M)'")
  private Double free;

  /**
   * JDK版本
   */
  @Column(name = "jvm_version", columnDefinition = "varchar(50) comment 'JDK版本'")
  private String version;

  /**
   * JDK路径
   */
  @Column(name = "jvm_home", columnDefinition = "varchar(255) comment 'JDK路径'")
  private String home;

  public JvmData() {
    super();
  }

  public JvmData(Jvm jvm) {
    this.total = jvm.getTotal();
    this.max = jvm.getMax();
    this.free = jvm.getFree();
    this.version = jvm.getVersion();
  }
}
