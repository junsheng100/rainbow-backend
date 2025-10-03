package com.rainbow.user.model;

import cn.hutool.core.lang.tree.TreeNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.user.model
 * @Filename：DeptUserTree
 * @Date：2025/9/16 16:11
 * @Describe:
 */
@Data
public class DeptUserTree  implements Serializable {

  private Long deptId;

  private String deptName;

  private Long parentId;

  private List<UserProfile> userList;


  private List<DeptUserTree> children;

  public DeptUserTree(){

  }

  public DeptUserTree(Long deptId,Long parentId,String deptName){
    this.deptId = deptId;
    this.parentId = parentId;
    this.deptName = deptName;
  }

  public DeptUserTree(Long deptId,Long parentId,String deptName,List<UserProfile> userList){
    this.deptId = deptId;
    this.parentId = parentId;
    this.deptName = deptName;
    this.userList = userList;
  }

}
