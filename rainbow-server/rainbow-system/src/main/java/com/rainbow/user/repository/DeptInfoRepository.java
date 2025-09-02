package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.DeptInfo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeptInfoRepository extends BaseRepository<DeptInfo,Long> {

  @Query("select t from DeptInfo t where t.status = '0' and t.deptId in (?1) order by orderNum ")
  List<DeptInfo> findInId(List<Long> idList);

  @Query("select t from DeptInfo t where t.status = '0' order by orderNum ")
  List<DeptInfo> findDeptInfoAll();


}
