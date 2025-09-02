package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysDictData;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public interface SysDictDataDao extends BaseDao<SysDictData,Long> {


  List<SysDictData> findInDictType(List<String> dictTypeList);

  List<SysDictData> findByDictType(@NotBlank(message = "字典类型不能为空") @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符") @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）") String dictType);
}
