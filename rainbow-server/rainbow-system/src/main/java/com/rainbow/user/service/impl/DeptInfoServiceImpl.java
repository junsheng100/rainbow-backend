package com.rainbow.user.service.impl;

import com.rainbow.base.constant.DataConstant;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.enums.PushTypeEnums;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.model.UserDeptInfo;
import com.rainbow.user.resource.DeptInfoDao;
import com.rainbow.user.resource.UserInfoDao;
import com.rainbow.user.service.DeptInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeptInfoServiceImpl extends BaseServiceImpl<DeptInfo, Long, DeptInfoDao> implements DeptInfoService {

  @Autowired
  private UserInfoDao userInfoDao;

  @Override
  public List<DeptInfo> findTreeView(BaseVo<DeptInfo> vo) {

    List<DeptInfo> list = list(vo);
    if (CollectionUtils.isNotEmpty(list)) {
      List<DeptInfo> all = baseDao.findAll();
      convertCollection(all);
      List<Long> idList = list.stream().map(DeptInfo::getDeptId).distinct().collect(Collectors.toList());
      list = all.stream().filter(a -> idList.contains(a.getDeptId())).collect(Collectors.toList());

      List<DeptInfo> parentLis = new ArrayList<>();
      getParentIdList(all, list, parentLis);
      if (CollectionUtils.isNotEmpty(parentLis)) {
        list.addAll(new ArrayList<>(new HashSet<>(parentLis)));
        list = new ArrayList<>(new HashSet<>(list));
      }
      for (DeptInfo deptInfo : list) {
        List<DeptInfo> children = list.stream().filter(a -> a.getParentId().equals(deptInfo.getDeptId())).collect(Collectors.toList());
        deptInfo.setChildren(children);
      }
      list = list.stream().filter(t -> DataConstant.MENU_ROOT.equals(t.getParentId())).collect(Collectors.toList());
      return list;

    }
    return list;
  }

  @Override
  public List<UserDeptInfo> findUserList(String pushType, List<Long> idList) {


    if (StringUtils.isBlank(pushType) || StringUtils.isBlank(pushType.trim()))
      return Collections.emptyList();
    List<DeptInfo> all = baseDao.findAll();
    PushTypeEnums type = PushTypeEnums.getByCode(pushType);
    List<UserInfo> userList = null;


    if(PushTypeEnums.ALL.getCode().equals(type.getCode())) {
      userList = userInfoDao.findUserAll();
    }

    if(PushTypeEnums.INCLUDE.getCode().equals(type.getCode())) {
      convertCollection(all);
      all.forEach(d -> {
        List<DeptInfo> children = all.stream().filter(a -> a.getParentId().equals(d.getDeptId())).collect(Collectors.toList());
        d.setChildren(children);
      });

      List<Long> deptIdList = all.stream().map(DeptInfo::getDeptId).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(idList)) {
        List<DeptInfo> deptInfoList = all.stream().filter(a -> idList.contains(a.getDeptId())).collect(Collectors.toList());
        deptIdList = new ArrayList<>();
        getDeptIdList(deptInfoList, deptIdList);
        deptIdList = new ArrayList<>(new HashSet<>(deptIdList));
      }

      userList = userInfoDao.findInDeptId(deptIdList);

    }

    if(PushTypeEnums.SELF.getCode().equals(type.getCode())) {
      userList = userInfoDao.findInDeptId(idList);
    }

    if (CollectionUtils.isNotEmpty(userList)) {
      List<UserDeptInfo> userDtoList = userList.stream().map(t -> {
        UserDeptInfo userDto = new UserDeptInfo();
        userDto.setUserId(t.getUserId());
        userDto.setUserName(t.getUserName());
        userDto.setNickname(t.getNickname());
        userDto.setDeptId(t.getDeptId());
        String deptName = all.stream().filter(a -> a.getDeptId().equals(t.getDeptId())).map(DeptInfo::getDeptName).collect(Collectors.joining(ChartEnum.COMMA.getCode()));
        userDto.setDeptName(deptName);

        return userDto;
      }).collect(Collectors.toList());
      return userDtoList;
    }

    return Collections.emptyList();
  }

  private void getDeptIdList(List<DeptInfo> deptInfoList, List<Long> idList) {

    if (CollectionUtils.isEmpty(deptInfoList))
      return;
    List<Long> deptIdList = deptInfoList.stream().map(DeptInfo::getDeptId).collect(Collectors.toList());
    idList.addAll(deptIdList);

    for (DeptInfo deptInfo : deptInfoList) {
      List<DeptInfo> children = deptInfo.getChildren();
      if (CollectionUtils.isNotEmpty(children)) {
        getDeptIdList(children, idList);
      }
    }

  }

  private void getParentIdList(List<DeptInfo> all, List<DeptInfo> list, List<DeptInfo> parentLis) {

    List<Long> pidList = CollectionUtils.isEmpty(list) ? null : list.stream().map(DeptInfo::getParentId).collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(pidList)) {
      parentLis.addAll(list);
      List<DeptInfo> prevList = all.stream().filter(a -> pidList.contains(a.getDeptId())).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(prevList)) {
        parentLis.addAll(prevList);
        getParentIdList(all, prevList, parentLis);
      }
    }

  }


  public void convertData(DeptInfo entity) {
    if (null != entity) {
      Long parentId = entity.getParentId();
      DeptInfo data = null == parentId ? null : get(parentId);
      entity.setParentName(null == data ? "" : data.getDeptName());
    }
  }

  public void convertCollection(List<DeptInfo> list) {
    if (CollectionUtils.isEmpty(list))
      return;
    List<Long> parentIdList = list.stream().filter(t -> null != t.getParentId())
            .filter(t -> !DataConstant.DEPT_ROOT.equals(t.getParentId()))
            .map(DeptInfo::getParentId).distinct().collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(parentIdList)) {
      List<DeptInfo> parentList = baseDao.findInId(parentIdList);
      list.stream().forEach(t -> {
        String parentName = CollectionUtils.isEmpty(parentList) ? "" : parentList.stream()
                .filter(a -> t.getParentId().equals(a.getDeptId()))
                .map(DeptInfo::getDeptName).collect(Collectors.joining(ChartEnum.COMMA.getCode()));
        t.setParentName(parentName);
      });
    }
  }


}
