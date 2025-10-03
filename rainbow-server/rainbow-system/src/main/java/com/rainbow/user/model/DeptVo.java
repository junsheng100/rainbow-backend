package com.rainbow.user.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.model
 * @Filename：DeptVo
 * @Date：2025/9/16 16:17
 * @Describe:
 */
@Data
public class DeptVo implements Serializable {

  private Long deptId;

  private String deptName;

}
