package com.rainbow.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.model
 * @Filename：DeptUserVo
 * @Describe:
 */
@Data
public class DeptUserVo implements Serializable {

  private String pushType;

  private List<Long> deptIdList;

}
