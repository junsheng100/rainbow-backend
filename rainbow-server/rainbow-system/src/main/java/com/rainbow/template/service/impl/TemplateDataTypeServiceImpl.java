package com.rainbow.template.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.resource.impl.DataManager;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.ExcelUtils;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.resource.SysResourceDao;
import com.rainbow.template.entity.TemplateDataType;
import com.rainbow.template.resource.TemplateDataTypeDao;
import com.rainbow.template.service.TemplateDataTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class TemplateDataTypeServiceImpl extends BaseServiceImpl<TemplateDataType, String, TemplateDataTypeDao> implements TemplateDataTypeService {

  @Autowired
  protected DataManager<TemplateDataType> dataManager;


  @Autowired
  private SysResourceDao resourceDao;
  @Autowired
  private ExcelUtils<TemplateDataType> excelUtils;

  @Override
  public PageData<TemplateDataType> pageList(CommonVo<String> vo) {
    Pageable pageable = dataManager.getCommonPageable(vo, TemplateDataType.class);
    PageData<TemplateDataType> pageData = baseDao.findPageList(vo.getData(), pageable);

    return pageData;
  }

  @Override
  public List<TemplateDataType> findAll() {
    return baseDao.findAll();
  }

  @Override
  public List<String> findDbGroup() {
    return baseDao.findDbGroup();
  }

  @Override
  public List<TemplateDataType> findByDbType(String dbType) {
    return baseDao.findByDbType(dbType);
  }

  @Override
  public Boolean readFile(String filePath) {
    if (StringUtils.isBlank(filePath))
      throw new BizException("资源文件不存在");

    File file = new File(filePath);

    if (!file.exists())
      throw new BizException("资源文件不存在");

    if (!file.isFile())
      throw new BizException("资源文件不存在");

    if (!file.canRead())
      throw new BizException("资源文件不可读");


    List<TemplateDataType> dataList = excelUtils.readExcel(filePath, TemplateDataType.class, 0);

    if(CollectionUtils.isNotEmpty(dataList)){
      Integer maxOrderNum = baseDao.getMaxOrderNum();
      maxOrderNum ++;
      for(TemplateDataType entity:dataList){
        Integer orderNum = entity.getOrderNum();
        orderNum = null==orderNum?maxOrderNum:orderNum;
        entity.setOrderNum(orderNum);
        baseDao.store(entity);
      }
    }

    return true;
  }

  @Override
  public Boolean delete(String id) {
    return baseDao.remove(id);
  }

  @Override
  public Boolean deleteInBatch(List<String> data) {
    return baseDao.removeInBatch(data);
  }


}
