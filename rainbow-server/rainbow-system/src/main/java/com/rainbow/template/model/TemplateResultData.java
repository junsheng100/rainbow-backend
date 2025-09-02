package com.rainbow.template.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateResultData implements Serializable {


  private String entityId;

  private String configId;

  private String srcContent;

  private String destContent;
}
