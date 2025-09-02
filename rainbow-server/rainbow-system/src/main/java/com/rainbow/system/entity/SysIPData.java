package com.rainbow.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rainbow.base.annotation.UnionKey;
import com.rainbow.base.entity.BaseEntity;
import com.rainbow.base.model.vo.AddressInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.system.entity
 * @Filename：SysIPAddress
 * @Describe:
 */
@Data
@Entity
@Table(name = "sys_ip_data")
@org.hibernate.annotations.Table(appliesTo = "sys_ip_data", comment = "登录 IP 地址")
@JsonIgnoreProperties({"fcu", "lcd", "lcu"})
public class SysIPData extends BaseEntity {

  @Id
  @Schema(title = "主键", type = "String")
  @Column(length = 20)
  private String id;

  @Column(length = 50)
  @Schema(title = "国家", type = "String")
  private String country;

  @Column(length = 50)
  @Schema(title = "城市", type = "String")
  private String city;

  @Column(length = 50)
  @Schema(title = "省份", type = "String")
  private String pro;

  @UnionKey
  @Column(length = 50)
  @Schema(title = "ip地址", type = "String")
  private String ipaddr;

  @Column(length = 128)
  @Schema(title = "", type = "String")
  private String location;

  public SysIPData() {
  }

  public SysIPData(String ipaddr, String country, String pro, String city,String location) {
    this.ipaddr = ipaddr;
    this.country = country;
    this.pro = pro;
    this.city = city;
    this.location = location;
  }

  public SysIPData(AddressInfo addressInfo) {
    this.ipaddr = addressInfo.getIp();
    this.country = addressInfo.getCountry();
    this.pro = addressInfo.getPro();
    this.city = addressInfo.getCity();
    this.location = addressInfo.getAddr();
  }

}
