package com.rainbow.user.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.user.entity.SysRole;
import com.rainbow.user.model.RoleTree;
import com.rainbow.user.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role/info")
@Tag(name = "角色管理")
public class SysRoleController extends BaseController<SysRole,Long, SysRoleService> {


  @Operation(description = "查询角色树列表")
  @PostMapping("/tree/list")
  public Result<List<RoleTree>> findListTree(@RequestBody CommonVo<Long[]> vo) {
    List<RoleTree> treeList = service.findListTree(vo.getData());
    return Result.success(treeList);
  }

  @Operation(description = "按ID查询角色树")
  @GetMapping("/tree/{roleId}")
  public Result<List<RoleTree>> findRoleTree(@PathVariable(name="roleId") Long roleId) {
    List<RoleTree> treeList = service.findListTree(roleId);
    return Result.success(treeList);
  }

}
