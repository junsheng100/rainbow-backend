package com.rainbow.system.controller;

import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.model.base.Result;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.model.vo.DictDataVo;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.system.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rainbow.base.model.base.Result.success;

@RestController
@RequestMapping("/dict/data")
@Tag(name = "数据字典值")
public class SysDictDataController extends BaseController<SysDictData,Long, SysDictDataService> {

  /**
   * 根据字典类型查询字典数据信息
   */
  @OperLog
  @Operation(description = "根据类型查数据")
  @GetMapping(value = "/type/{dictType}")
  public Result<List<DictDataVo>> dictType(@PathVariable String dictType) {
    SysDictData data  = new SysDictData();
    data.setStatus(UseStatus.NO.getCode());
    data.setDictType(dictType);
    BaseVo<SysDictData>  vo = new BaseVo<>(data);

    List<SysDictData> list  = service.list(vo);

    List<DictDataVo> dataVoList = CollectionUtils.isEmpty(list)?new ArrayList<>():list.stream().map(t->{
      DictDataVo dataVo = new DictDataVo();
      BeanUtils.copyProperties(t,dataVo, CommonUtils.getNullPropertyNames(t));
      return dataVo;
    }).collect(Collectors.toList());

    return success(dataVoList);
  }

}
