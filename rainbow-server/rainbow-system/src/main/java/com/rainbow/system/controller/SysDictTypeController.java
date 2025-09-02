package com.rainbow.system.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.system.entity.SysDictType;
import com.rainbow.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rainbow.base.model.base.Result.success;

@RestController
@RequestMapping("/dict/type")
@Tag(name = "数据字典")
public class SysDictTypeController extends BaseController<SysDictType,Long, SysDictTypeService> {


  @OperLog
  @Operation(description = "删除数据")
  @DeleteMapping("/delete/{id}")
  public Result<Boolean> deleteIdList(@PathVariable(name = "id") Long... id) {
    Boolean flag = service.deleteIdList(id);
    return Result.success(flag);
  }


  @Operation(description = "数据状态管理")
  @PutMapping("/status/{dictId}/{disabled}")
  public Result<Boolean> changeStatus(@PathVariable(name = "dictId") Long dictId,
                                      @PathVariable(name = "disabled") String disabled) {
    Boolean flag = service.changeDisabled(dictId,disabled);
    return Result.success(flag);
  }

  /**
   * 刷新字典缓存
   */
//  @PreAuthorize("@ss.hasPermi('system:dict:remove')")


  @Operation(description = "刷新字典缓存")
  @DeleteMapping("/refreshCache")
  public Result<Boolean> refreshCache() {
    service.resetDictCache();
    return success(true);
  }


  /**
   * 获取字典选择框列表
   */
  @Operation(description = "获取择框列表")
  @GetMapping("/optionselect")
  public Result< List<SysDictType>> optionselect() {

    SysDictType dictType = new SysDictType();
    dictType.setStatus("0");

    BaseVo<SysDictType> vo = new BaseVo<>(dictType);
    List<SysDictType> dictTypes = service.list(vo);

    return success(dictTypes);
  }

}
