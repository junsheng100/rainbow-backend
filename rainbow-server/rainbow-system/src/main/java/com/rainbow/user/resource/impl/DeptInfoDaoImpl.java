package com.rainbow.user.resource.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.repository.DeptInfoRepository;
import com.rainbow.user.repository.UserInfoRepository;
import com.rainbow.user.resource.DeptInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DeptInfoDaoImpl extends BaseDaoImpl<DeptInfo, Long, DeptInfoRepository> implements DeptInfoDao {


  @Autowired
  private UserInfoRepository userInfoRepository;

  @Override
  public List<DeptInfo> findInId(List<Long> parentIdList) {
    if (CollectionUtils.isEmpty(parentIdList))
      return Collections.emptyList();
    return jpaRepository.findInId(parentIdList);
  }

  @Override
  public List<DeptInfo> findAll() {
    return jpaRepository.findDeptInfoAll();
  }

  @Override
  public List<DeptInfo> findDeptInfoAll() {
    List<DeptInfo> all = findAll();
    if (CollectionUtils.isNotEmpty(all)) {
      for (DeptInfo deptInfo : all) {
        List<DeptInfo> children = all.stream().filter(t -> t.getParentId().equals(deptInfo.getDeptId())).collect(Collectors.toList());
        deptInfo.setChildren(children);
      }
    }
    return all;
  }


  @Override
  public Map<Long, String> findDeptName(List<Long> deptIdList) {
    Map<Long, String> mp = new HashMap<>();
    if (CollectionUtils.isNotEmpty(deptIdList)) {
      List<DeptInfo> list = jpaRepository.findInId(deptIdList);
      if (CollectionUtils.isNotEmpty(list)) {
        list.stream().forEach(t -> {
          mp.put(t.getDeptId(), t.getDeptName());
        });
      }
    }
    return mp;
  }

  @Override
  public List<String> findUserIdByDeptId(Integer type, List<Long> deptIds) {
    List<String> list = null;
    if (null == type)
      return Collections.emptyList();

    List<Long> deptIdList = new ArrayList<>();

    List<DeptInfo> all = findDeptInfoAll();
    List<Long> allIdList  = all.stream().map(DeptInfo::getDeptId).collect(Collectors.toList());

    if (type == 0) {
      deptIdList = all.stream().map(DeptInfo::getDeptId).collect(Collectors.toList());
    } else if (type == 1) {
      if(CollectionUtils.isNotEmpty(deptIds)){
        List<DeptInfo> deptInfoList = all.stream().filter(a -> deptIds.contains(a.getDeptId())).collect(Collectors.toList());
        getDeptIdList(deptInfoList, deptIdList);
      }else{
        deptIdList = allIdList;
      }
    } else if (type == 2) {
      deptIdList = CollectionUtils.isEmpty(deptIds) ? allIdList : deptIds;
    }else{
      throw new BizException("未知的范围");
    }
    list = userInfoRepository.findUserIdInDeptId(deptIdList);
    return list;
  }


  public void getDeptIdList(List<DeptInfo> deptInfoList, List<Long> deptIdList) {
    deptIdList = CollectionUtils.isEmpty(deptIdList) ? new ArrayList<>() : deptIdList;
    if (CollectionUtils.isNotEmpty(deptInfoList)) {
      for (DeptInfo deptInfo : deptInfoList) {
        deptIdList.add(deptInfo.getDeptId());
        if (CollectionUtils.isNotEmpty(deptInfo.getChildren())) {
          getDeptIdList(deptInfo.getChildren(), deptIdList);
        }
      }
    }
  }


}
