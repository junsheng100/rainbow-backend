package com.rainbow.system.service.impl;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.system.entity.SysDictType;
import com.rainbow.system.resource.SysDictDataDao;
import com.rainbow.system.resource.SysDictTypeDao;
import com.rainbow.system.service.SysDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictData,Long, SysDictDataDao> implements SysDictDataService {

  @Autowired
  private SysDictTypeDao typeDao;

  @Override
  public SysDictData store(@Valid SysDictData entity) {
    baseDao.store(entity);
    if(CommonUtils.hashUseStatus(entity,UseStatus.NO)) {
      SysDictType type = typeDao.findByDictType(entity.getDictType());
      Assert.notNull(type, "字典类型不存在");
      if (!CommonUtils.hashUseStatus(type,UseStatus.NO)) {
         type.setStatus(UseStatus.NO.getCode());
         typeDao.save(type);
      }
    }

    return entity;
  }

  @Override
  public SysDictData save(SysDictData entity) {
    throw new BizException("方法已过期并不能适用");
  }

  @Override
  public Boolean delete(Long id) {
    return baseDao.remove(id);
  }


  @Override
  public Boolean deleteInBatch(List<Long> data) {
    return baseDao.removeInBatch(data);
  }

  public void convertData(SysDictData entity) {
    if (null != entity) {
      SysDictType type = StringUtils.isBlank(entity.getDictType()) ? null : typeDao.findByDictType(entity.getDictType());
      entity.setDictName(null == type ? "" : type.getDictName());
    }
  }

  public void convertCollection(List<SysDictData> list) {
    if (CollectionUtils.isEmpty(list))
      return;
    List<String> dictTypeList = list.stream().filter(t -> StringUtils.isNotBlank(t.getDictType())).map(SysDictData::getDictType).distinct().collect(Collectors.toList());
    List<SysDictType> typeList = typeDao.findInDictType(dictTypeList);

    list.stream().forEach(data -> {
      String dictName = typeList.stream().filter(t -> data.getDictType().equals(t.getDictType())).map(SysDictType::getDictName).distinct().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
      data.setDictName(dictName);
    });
  }

}
