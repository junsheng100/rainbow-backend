package com.rainbow.template.resource;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.resource.BaseDao;
import com.rainbow.template.entity.TemplateConfig;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TemplateConfigDao extends BaseDao<TemplateConfig,String> {


  List<TemplateConfig> findInId(List<String> configIdList);

  List<TemplateConfig> findDataList();

  PageData<TemplateConfig> findPageList(String data, Pageable pageable);
}
