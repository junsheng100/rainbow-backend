package com.rainbow.system.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyVo implements Serializable {

  private String id;

  private Integer stage;

  private String reply;

}
