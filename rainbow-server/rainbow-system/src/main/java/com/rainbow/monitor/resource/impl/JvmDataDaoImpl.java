package com.rainbow.monitor.resource.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.model.vo.PageDomain;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.monitor.entity.JvmData;
import com.rainbow.monitor.model.server.ServerInfo;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.repository.JvmDataRepository;
import com.rainbow.monitor.resource.JvmDataDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JvmDataDaoImpl extends BaseDaoImpl<JvmData,String, JvmDataRepository> implements JvmDataDao {


  @Override
  public JvmData saveServerInfo(String sysId, ServerInfo server, Date takeTime) {
    if (StringUtils.isBlank(sysId))
      return null;
    if(null == server || null == server.getJvm())
      return null;
    takeTime = null==takeTime?new Date():takeTime;


    JvmData entity = new JvmData(server.getJvm());
    entity.setSysId(sysId);
    entity.setTakeTime(takeTime);

    return super.save(entity);
  }

  @Override
  public boolean removeInSysId(List<String> sysIdList) {
    if(CollectionUtils.isNotEmpty(sysIdList)){
      super.jpaRepository.removeInSysId(sysIdList);
      return true;
    }
    return false;
  }

  @Override
  public void cleanAll() {
    jpaRepository.deleteAllInBatch();
  }

  @Override
  public PageData<JvmData> pageData(CommonVo<DataVo> vo) {
    Integer pageNo = vo.getPageNo() - 1 < 0 ? 0 : vo.getPageNo() - 1;
    Integer pageSize = vo.getPageSize();
    Sort sort = PageDomain.getRequestSort(vo.getSort());
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    DataVo data = vo.getData();

    Page<JvmData> page = jpaRepository.findPageData(data, pageable);

    PageData<JvmData> pageData = new PageData<>(page);

    return pageData;
  }
}
