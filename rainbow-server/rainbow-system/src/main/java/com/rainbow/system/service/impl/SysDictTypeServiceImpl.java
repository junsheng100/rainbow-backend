package com.rainbow.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.model.vo.DictDataVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.SysDictData;
import com.rainbow.system.entity.SysDictType;
import com.rainbow.system.resource.SysDictDataDao;
import com.rainbow.system.resource.SysDictTypeDao;
import com.rainbow.system.service.SysDictTypeService;
import com.rainbow.user.utils.DictUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictType,Long, SysDictTypeDao> implements SysDictTypeService {

  @Autowired
  private SysDictDataDao dataDao;


  @Override
  public SysDictType store(@Valid SysDictType entity) {

    SysDictType old = baseDao.check(entity);
    if(null != old){
      String dictType = old.getDictType();
      List<SysDictData> dataList = dataDao.findByDictType(dictType);
      if(CollectionUtils.isNotEmpty(dataList)){
        dataList.stream().forEach(data->{
          data.setDictType(entity.getDictType());
          data.setStatus(entity.getStatus());
          dataDao.store(data);
        });
      }
    }
    baseDao.store(entity);

    return entity;
  }

  @Override
  public SysDictType save(SysDictType entity) {
    throw new BizException("方法已过期并不能适用");
  }


  @Override
  public void resetDictCache() {
    clearDictCache();
    loadingDictCache();
  }

  public void loadingDictCache() {

    SysDictData dictData = new SysDictData();
    dictData.setStatus("0");

    BaseVo<SysDictData> vo = new BaseVo<>(dictData);

    Map<String, List<SysDictData>> dictDataMap = dataDao.list(vo).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
    for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
      List<DictDataVo> dictDataVoList = JSON.parseArray(JSON.toJSONString(entry.getValue()), DictDataVo.class);
      DictUtils.setDictCache(entry.getKey(), dictDataVoList.stream().sorted(Comparator.comparing(DictDataVo::getDictSort)).collect(Collectors.toList()));
    }

  }

  @Override
  public Boolean delete(Long id) {
    return deleteIdList(id);
  }

  @Override
  public Boolean deleteInBatch(List<Long> idList) {
    Long[] ids = idList.toArray(new Long[idList.size()]);
    return deleteIdList(ids);
  }

  @Transactional
  @Override
  public Boolean deleteIdList(Long... idList) {
    if (null == idList || idList.length == 0)
      return false;
    List<Long> dictIdList = Arrays.asList(idList);
    List<SysDictType> typeList = super.baseDao.findInDictId(dictIdList);

    if (CollectionUtils.isNotEmpty(typeList)) {
      List<String> dictTypeList = typeList.stream().map(SysDictType::getDictType).distinct().collect(Collectors.toList());

      List<SysDictData> dataList = dataDao.findInDictType(dictTypeList);

      if (CollectionUtils.isNotEmpty(dataList)) {
        dictTypeList = dataList.stream().map(SysDictData::getDictType).distinct().collect(Collectors.toList());
        List<String> finalDictTypeList = dictTypeList;
        String typeNameList = typeList.stream().filter(t -> finalDictTypeList.contains(t.getDictType())).map(SysDictType::getDictName).distinct().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
        throw new BizException(String.format("%1$s已分配,不能删除", typeNameList));
      }

      for (SysDictType type : typeList) {
        super.baseDao.remove(type.getDictId());
      }
      return true;
    }
    return false;
  }

  @Override
  public Boolean changeDisabled(Long dictId, String disabled) {
    Assert.notNull(dictId, "数据类型ID不能为空");
    Assert.notNull(disabled, "状态码不能为空");
    Long stateCount = Arrays.stream(UseStatus.values()).filter(t -> disabled.equals(t.getCode())).count();
    Assert.isTrue(stateCount == 0, "转态码不正确");
    SysDictType type = baseDao.get(dictId);
    Assert.notNull(type, "数据类型不存在");

    type.setDisabled(disabled);
    baseDao.store(type);

    List<SysDictData> dataList = dataDao.findByDictType(type.getDictType());
    if (CollectionUtils.isNotEmpty(dataList)) {
      dataList.stream().forEach(data -> {
        data.setDictName(disabled);
        dataDao.store(data);
      });

    }

    return null;
  }


  private void clearDictCache() {
    DictUtils.clearDictCache();
  }
}
