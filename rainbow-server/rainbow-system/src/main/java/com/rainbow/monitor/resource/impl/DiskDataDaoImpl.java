package com.rainbow.monitor.resource.impl;

import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.vo.CommonVo;
import com.rainbow.base.model.vo.PageDomain;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.monitor.entity.DiskData;
import com.rainbow.monitor.model.server.ServerInfo;
import com.rainbow.monitor.model.server.SysFile;
import com.rainbow.monitor.model.vo.DataVo;
import com.rainbow.monitor.repository.DiskDataRepository;
import com.rainbow.monitor.resource.DiskDataDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class DiskDataDaoImpl extends BaseDaoImpl<DiskData, String, DiskDataRepository> implements DiskDataDao {


  @Override
  public List<DiskData> saveServerInfo(String sysId, ServerInfo server, Date takeTime) {
    if (StringUtils.isBlank(sysId))
      return null;
    if (null == server || CollectionUtils.isEmpty(server.getSysFiles()))
      return null;

    takeTime = null == takeTime ? new Date() : takeTime;

    List<DiskData> list = new ArrayList<>();
    for (SysFile sysFile : server.getSysFiles()) {
      DiskData entity = new DiskData(sysFile);
      entity.setSysId(sysId);
      entity.setTakeTime(takeTime);
      super.save(entity);

      list.add(entity);
    }

    return list;
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
  public PageData<DiskData> pageData(CommonVo<DataVo> vo) {
    Integer pageNo = vo.getPageNo() - 1 < 0 ? 0 : vo.getPageNo() - 1;
    Integer pageSize = vo.getPageSize();
    Sort sort = PageDomain.getRequestSort(vo.getSort());
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    DataVo data = vo.getData();

    Page<DiskData> page = jpaRepository.findPageData(data, pageable);

    PageData<DiskData> pageData = new PageData<>(page);

    return pageData;
  }
}
