package com.rainbow.template.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TemplateDataParams implements Serializable {

  private List<String>  fileNameList;
  private List<String>  entityIdList;
  private List<String>  configIdList;


}
