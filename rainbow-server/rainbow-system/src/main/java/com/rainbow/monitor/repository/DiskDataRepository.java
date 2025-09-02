package com.rainbow.monitor.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.monitor.entity.DiskData;
import com.rainbow.monitor.model.vo.DataVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiskDataRepository extends BaseRepository<DiskData,String> {

  @Modifying
  @Query("delete from DiskData where sysId in (?1)")
  void removeInSysId(List<String> sysIdList);


  @Query("select t from DiskData t , SysData d where t.sysId = d.id " +
          "   and (:#{#data.keyword} is null " +
          "     or LOWER(d.computerName) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(d.computerIp) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(d.osName) like concat('%',LOWER(:#{#data.keyword}),'%')" +
          "     or LOWER(d.osArch) like concat('%',LOWER(:#{#data.keyword}),'%') )" +
          "   and (:#{#data.startTime} is null or t.takeTime >= :#{#data.startTime}) " +
          "   and (:#{#data.endTime} is null or t.takeTime <= :#{#data.endTime}) " )
  Page<DiskData> findPageData(DataVo data, Pageable pageable);

}
