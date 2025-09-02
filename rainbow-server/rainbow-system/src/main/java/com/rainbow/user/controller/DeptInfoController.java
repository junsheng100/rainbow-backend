package com.rainbow.user.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.model.DeptUserVo;
import com.rainbow.user.model.UserDeptInfo;
import com.rainbow.user.service.DeptInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dept")
@Tag(name = "部门管理")
public class DeptInfoController extends BaseController<DeptInfo,Long, DeptInfoService> {


  
  @Operation(summary = "部门树")
  @PostMapping("/tree/view")
  public Result<List<DeptInfo>> findTreeView(@RequestBody BaseVo<DeptInfo> vo) {
    List<DeptInfo> treeList = service.findTreeView(vo);
    return Result.success(treeList);
  }


  @Operation(summary = "部门人员列表")
  @PostMapping("/user")
  public Result<List<UserDeptInfo>> findUserList(@RequestBody DeptUserVo vo) {
    String pushType = vo.getPushType();
    List<Long> deptIds = vo.getDeptIdList();
    List<UserDeptInfo> treeList = service.findUserList(pushType,deptIds);
    return Result.success(treeList);
  }





}
