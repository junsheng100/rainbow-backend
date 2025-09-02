package com.rainbow.template.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TemplateResultVo implements Serializable {

  private String entityId;

  public List<String> configIdList;

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public List<String> getConfigIdList() {
    return configIdList;
  }

  public void setConfigIdList(List<String> configIdList) {
    this.configIdList = configIdList;
  }
}
