package com.rainbow.user.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.user.entity.SysMenu;
import com.rainbow.user.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Tag(name = "菜单管理")
public class SysMenuController extends BaseController<SysMenu,Long, SysMenuService> {


  @Operation(description = "查询类型树")
  @GetMapping("/tree/type/{menuType}")
  public Result<List<SysMenu>> findTreeList(@PathVariable(name = "menuType") String menuType) {
    List<SysMenu> treeList = service.findTreeList(menuType);
    return Result.success(treeList);
  }


  @Operation(description = "查询子菜单")
  @GetMapping("/child/{parentId}")
  public Result<List<SysMenu>> findChildren(@PathVariable(name = "parentId") Long  parentId) {
    List<SysMenu> treeList = service.findChildren(parentId);
    return Result.success(treeList);
  }

  @Operation(description = "查询树视图")
  @PostMapping("/tree/view")
  public Result<List<SysMenu>> findMenuTreeView(@RequestBody BaseVo<SysMenu> vo) {

    List<SysMenu> treeList = service.findMenuTreeView(vo);
    return Result.success(treeList);
  }

  @Operation(description = "查询树列表")
  @GetMapping("/tree/list")
  public Result<List<SysMenu>> findMenuTree() {
    List<SysMenu> treeList = service.findMenuTree();
    return Result.success(treeList);
  }


}
