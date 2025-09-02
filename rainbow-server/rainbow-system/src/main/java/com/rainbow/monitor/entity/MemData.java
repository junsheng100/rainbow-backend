package com.rainbow.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rainbow.monitor.model.entity.ServerDataEntity;
import com.rainbow.monitor.model.server.Mem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "server_mem_data")
@org.hibernate.annotations.Table(appliesTo = "server_mem_data", comment = "server mem 数据表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"fcu", "lcd", "lcu"})
public class MemData extends ServerDataEntity {


  @Column(name = "mem_total", columnDefinition = "double(10,2) comment '内存总量'")
  private Double total;


  @Column(name = "mem_used", columnDefinition = "double(10,2) comment '已用内存'")
  private Double used;

  @Column(name = "mem_free", columnDefinition = "double(10,2) comment '剩余内存'")
  private Double free;

  public MemData() {
    super();
  }

  public MemData(Mem mem) {
    super();
    this.total = mem.getTotal();
    this.used = mem.getUsed();
    this.free = mem.getFree();
  }

}
