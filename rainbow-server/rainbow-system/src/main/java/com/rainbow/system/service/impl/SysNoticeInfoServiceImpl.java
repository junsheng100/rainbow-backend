package com.rainbow.system.service.impl;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.base.PageData;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.model.vo.PageDomain;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.SysNoticeInfo;
import com.rainbow.system.entity.SysNoticePush;
import com.rainbow.system.model.dto.NoticePushDto;
import com.rainbow.system.resource.SysNoticeInfoDao;
import com.rainbow.system.resource.SysNoticePushDao;
import com.rainbow.system.service.SysNoticeInfoService;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.resource.DeptInfoDao;
import com.rainbow.user.resource.UserInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysNoticeInfoServiceImpl extends BaseServiceImpl<SysNoticeInfo, Long, SysNoticeInfoDao> implements SysNoticeInfoService {

  @Autowired
  private SysNoticePushDao pushDao;
  @Autowired
  private DeptInfoDao deptInfoDao;
  @Autowired
  private UserInfoDao userDao;

  public final String COMMA = ChartEnum.COMMA.getCode();


  @Override
  public SysNoticeInfo read(Long id) {
    SysNoticeInfo entity = get(id);
    pushDao.read(entity);
    return entity;
  }

  @Override
  public PageData<SysNoticeInfo> pageWill(PageDomain page) {
    BaseVo<SysNoticeInfo> vo = new BaseVo<>();
    vo.setPageNo(page.getPageNo());
    vo.setPageSize(page.getPageSize());
//    vo.setSort("pushTime, desc");

    SysNoticeInfo entity = new SysNoticeInfo();
    LoginUser user = baseDao.getLoginUser();
    entity.setUserIds(user.getUserId());

    vo.setData(entity);

    PageData<SysNoticeInfo> pageData = baseDao.page(vo);
    convertCollection(pageData.getContent());
    return pageData;
  }


  @Override
  public SysNoticeInfo store(@Valid SysNoticeInfo entity) {

    baseDao.store(entity);

    return entity;
  }

  @Override
  public void parameters(BaseVo<SysNoticeInfo> vo) {
    try {
      LoginUser user = baseDao.getLoginUser();
      SysNoticeInfo entity = vo.getData();
      if (!super.isAdmin()) {
        entity.setUserIds(user.getUserId());
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new BizException(e.getMessage());
    }
  }

  @Override
  public Boolean deleteInBatch(List<Long> data) {

    if (CollectionUtils.isNotEmpty(data)) {
      for (int i = 0; i < data.size(); i++) {
        String value = String.valueOf(data.get(i));
        Long id = Long.valueOf(value);
        SysNoticeInfo info = get(id);
        info.setStatus(UseStatus.DEL.getCode());
        baseDao.save(info);
      }
      return true;
    }
    return false;
  }

  public void convertData(SysNoticeInfo entity) {
    if (null != entity) {
      NoticePushDto noticePushDto = pushDao.getPushData(entity.getNoticeId());
      String deptNames = getDeptNames(entity.getDeptIds());
      String userNames = getUserNames(entity.getUserIds());
      if (null != noticePushDto) {
        entity.setDeptCount(noticePushDto.getDeptCount());
        entity.setUserCount(noticePushDto.getUserCount());
        entity.setReadCount(noticePushDto.getReadCount());
      }
      entity.setDeptNames(deptNames);
      entity.setUserNames(userNames);
    }
  }


  public void convertCollection(List<SysNoticeInfo> list) {

    if (CollectionUtils.isNotEmpty(list)) {
      List<Long> idList = list.stream().map(SysNoticeInfo::getNoticeId).distinct().collect(Collectors.toList());
      List<SysNoticePush> pushList = pushDao.findInNoticeId(idList);
      Map<Long, NoticePushDto> dtoMap = pushDao.findPushData(pushList);

      List<DeptInfo> deptInfoList = getDeptInfoList(list);
      List<UserInfo> userInfoList = getUserInfoList(list);

      list.forEach(item -> {
        NoticePushDto dto = dtoMap.get(item.getNoticeId());
        if (null != dto) {
          item.setDeptCount(dto.getDeptCount());
          item.setUserCount(dto.getUserCount());
          item.setReadCount(dto.getReadCount());

          if (CollectionUtils.isNotEmpty(dto.getDeptIds())) {
            item.setDeptNames(deptInfoList.stream().filter(d -> dto.getDeptIds().contains(d.getDeptId())).map(DeptInfo::getDeptName).collect(Collectors.joining(COMMA)));
          }
          if (CollectionUtils.isNotEmpty(dto.getUserIds())) {
            item.setUserNames(userInfoList.stream().filter(u -> dto.getUserIds().contains(u.getUserId())).map(UserInfo::getUserName).collect(Collectors.joining(COMMA)));
          }
        }

      });

    }

  }

  private List<UserInfo> getUserInfoList(List<SysNoticeInfo> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      List<String> userIdList = new ArrayList<>();
      list.stream().forEach(item -> {
        String userIds = item.getUserIds();
        if (StringUtils.isNotBlank(userIds)) {
          userIdList.addAll(Arrays.asList(userIds.toString().split(COMMA)));
        }
      });
      return userDao.findInUserId(userIdList);
    }
    return null;
  }

  private List<DeptInfo> getDeptInfoList(List<SysNoticeInfo> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      List<Long> deptIdList = new ArrayList<>();
      list.stream().forEach(item -> {
        String deptIds = item.getDeptIds();
        if (StringUtils.isNotBlank(deptIds)) {
          deptIdList.addAll(Arrays.asList(deptIds.toString().split(COMMA)).stream().map(Long::valueOf).collect(Collectors.toList()));
        }
      });
      return deptInfoDao.findInId(deptIdList);
    }
    return null;
  }

  private String getDeptNames(String deptIds) {

    if (StringUtils.isNotBlank(deptIds)) {
      List<Long> idList = Arrays.asList(deptIds.toString().split(COMMA)).stream().map(Long::valueOf).collect(Collectors.toList());
      List<DeptInfo> deptInfoList = deptInfoDao.findInId(idList);
      if (CollectionUtils.isNotEmpty(deptInfoList)) {
        List<String> deptNames = deptInfoList.stream().map(DeptInfo::getDeptName).collect(Collectors.toList());
        return String.join(COMMA, deptNames);
      }
    }
    return null;
  }

  private String getUserNames(String userIds) {

    if (StringUtils.isNotBlank(userIds)) {
      List<String> idList = Arrays.asList(userIds.toString().split(COMMA));
      List<UserInfo> userList = userDao.findInUserId(idList);
      if (CollectionUtils.isNotEmpty(userList)) {
        List<String> userNameList = userList.stream().map(UserInfo::getUserName).collect(Collectors.toList());
        return String.join(COMMA, userNameList);
      }
    }
    return null;
  }


}
