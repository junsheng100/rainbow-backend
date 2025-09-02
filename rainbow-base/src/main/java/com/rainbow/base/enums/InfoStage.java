package com.rainbow.base.enums;

public enum InfoStage {

  WAIT("待推送"), PUSH("已推送"), OVER("已完成");

  private final String description;

  InfoStage(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
