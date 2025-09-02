package com.rainbow.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MenuItem implements Serializable {

  private Long id;
  private Long parentId;
  private String name;
  private String title;
  private String icon;
  private String path;
  private String component;
  private String redirect;
  private Boolean isHidden;
  private Integer orderNum;
  private String type;// 'CATALOG' | 'MENU' | 'BUTTON';  // 菜单类型：目录、菜单、按钮
  private String perms;  // 权限标识
  private Boolean status;
  private List<MenuItem> children;
}
