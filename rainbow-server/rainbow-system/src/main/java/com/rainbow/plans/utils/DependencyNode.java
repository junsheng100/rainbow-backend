package com.rainbow.plans.utils;

import lombok.Data;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.utils
 * @Filename：DependencyNode
 * @Date：2025/9/27 18:24
 * @Describe:
 */
@Data
public class DependencyNode {
  private String val;
  private DependencyNode next;

  public DependencyNode(String val) {
    this.val = val;
    this.next = null;
  }
  public DependencyNode(String val,String nextVal) {
    this.val = val;
    this.next = new DependencyNode(nextVal);
  }
}
