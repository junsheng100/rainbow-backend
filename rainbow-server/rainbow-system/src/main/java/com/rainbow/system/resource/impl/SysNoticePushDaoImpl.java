package com.rainbow.system.resource.impl;

import com.rainbow.base.enums.InfoStage;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.DateTools;
import com.rainbow.system.entity.SysNoticeInfo;
import com.rainbow.system.entity.SysNoticePush;
import com.rainbow.system.enums.PushTimeType;
import com.rainbow.system.model.dto.NoticePushDto;
import com.rainbow.system.repository.SysNoticeInfoRepository;
import com.rainbow.system.repository.SysNoticePushRepository;
import com.rainbow.system.resource.SysNoticePushDao;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SysNoticePushDaoImpl extends BaseDaoImpl<SysNoticePush, String, SysNoticePushRepository> implements SysNoticePushDao {

  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private SysNoticeInfoRepository noticeInfoRepository;


  @Override
  public boolean savePushData(NoticePushDto info) {
    if (null == info)
      return false;

    LocalDateTime pushTime = info.getPushTime();
    if (null == pushTime)
      return false;

    PushTimeType timeType = info.getPushTimeType();
    if (PushTimeType.AFTER.equals(timeType)) {
      if (pushTime.isBefore(LocalDateTime.now())) {
        throw new BizException("推送时间不能小于当前时间");
      }
    }

    String userName = getUserName();
    LocalDateTime now = LocalDateTime.now();
    Long noticeId = info.getNoticeId();

    Assert.notNull(noticeId, "消息/公告ID不能为空");
    SysNoticeInfo noticeInfo = noticeInfoRepository.getById(noticeId);
    Assert.notNull(noticeInfo, "消息/公告数据为空");
    noticeInfo.setStage(InfoStage.PUSH);
    noticeInfo.setStatus(UseStatus.NO.getCode());
    noticeInfo.setLcu(userName);
    noticeInfo.setLcd(now);
    noticeInfo.setPushType(info.getPushType());
    noticeInfo.setPushTime(pushTime);

    List<SysNoticePush> pushList = super.jpaRepository.findByNoticeId(noticeId);

    if (CollectionUtils.isNotEmpty(pushList))
      super.jpaRepository.deleteInBatch(pushList);

    List<String> userIdLit = info.getUserIds();
    if (CollectionUtils.isNotEmpty(userIdLit)) {
      List<UserInfo> userInfoList = userInfoRepository.findInUserId(userIdLit);
      String userIds =  userInfoList.stream().map(UserInfo::getUserId).distinct().collect(Collectors.joining(","));
      String deptIds = userInfoList.stream().map(u->{return u.getDeptId().toString();}).distinct().collect(Collectors.joining(","));
      noticeInfo.setUserIds(userIds);
      noticeInfo.setDeptIds(deptIds);
      noticeInfoRepository.save(noticeInfo);
      saveUserInfo(noticeId, noticeInfo, userInfoList);
      return true;
    } else {
      throw new BizException("用户ID不能为空");
    }

  }

  public void saveUserInfo(Long noticeId, SysNoticeInfo noticeInfo, List<UserInfo> userInfoList) {

    if (CollectionUtils.isNotEmpty(userInfoList)) {
      for (UserInfo userInfo : userInfoList) {
        SysNoticePush push = new SysNoticePush();
        push.setUserId(userInfo.getUserId());
        push.setPushTime(noticeInfo.getPushTime());
        push.setNoticeId(noticeId);
        push.setDeptId(userInfo.getDeptId());
        push.setIsPush(UseStatus.NO.getData());
        push.setIsRead(UseStatus.NO.getData());
        push.setPushType(noticeInfo.getPushType());
        push.setReadCount(0);
        super.store(push);
      }
    }
  }

  @Override
  public NoticePushDto getPushData(Long noticeId) {
    if (null == noticeId)
      return null;
    List<SysNoticePush> list = jpaRepository.findByNoticeId(noticeId);

    if (CollectionUtils.isEmpty(list))
      return null;
    LocalDateTime pushTime = list.stream().map(SysNoticePush::getPushTime).findFirst().orElse(null);
    List<Long> deptIds = list.stream().map(SysNoticePush::getDeptId).collect(Collectors.toList());
    List<String> userIds = list.stream().map(SysNoticePush::getUserId).collect(Collectors.toList());

    NoticePushDto dto = new NoticePushDto(noticeId, pushTime, deptIds, userIds);

    Integer readCount = list.stream().map(t -> {
      return null == t.getIsRead() ? 0 : (t.getIsRead() == 0 ? 0 : 1);
    }).reduce(0, Integer::sum);

    dto.setReadCount(null == readCount ? 0 : readCount.intValue());

    return dto;
  }

  @Override
  public List<SysNoticePush> findInNoticeId(List<Long> noticeIdList) {
    return CollectionUtils.isEmpty(noticeIdList) ? null : jpaRepository.findInNoticeId(noticeIdList);
  }


  public Map<Long, NoticePushDto> findPushData(List<SysNoticePush> list) {

    Map<Long, NoticePushDto> map = new HashMap<>();
    if (CollectionUtils.isNotEmpty(list)) {
      Map<Long, List<SysNoticePush>> group = list.stream().collect(Collectors.groupingBy(SysNoticePush::getNoticeId));

      for (Map.Entry<Long, List<SysNoticePush>> entry : group.entrySet()) {
        NoticePushDto dto = new NoticePushDto();
        List<SysNoticePush> dataList = entry.getValue();
        dto.setNoticeId(entry.getKey());
        LocalDateTime pushTime = dataList.stream().map(SysNoticePush::getPushTime).findFirst().orElse(null);
        List<Long> deptIds =  getDeptIdList(dataList);
        List<String> userIds = getUserIdList(dataList);


        Integer readCount = dataList.stream().map(t -> {
          return null == t.getIsRead() ? 0 : (t.getIsRead() == 0 ? 0 : 1);
        }).reduce(0, Integer::sum);

        dto.setPushTime(pushTime);
        dto.setDeptIds(deptIds);
        dto.setUserIds(userIds);
        dto.setDeptCount(CollectionUtils.isEmpty(deptIds) ? 0 : deptIds.size());
        dto.setUserCount(CollectionUtils.isEmpty(userIds) ? 0 : userIds.size());
        dto.setReadCount(null == readCount ? 0 : readCount.intValue());

        map.put(entry.getKey(), dto);
      }

    }
    return map;
  }

  private List<Long> getDeptIdList(List<SysNoticePush> dataList) {

    List<Long> deptIds = new ArrayList<>();
    dataList.stream().forEach(t -> {
      Long deptId = t.getDeptId();
      if (null != deptId) {
        deptIds.add(deptId);
      }
    });
    return deptIds;
  }

  private List<String> getUserIdList(List<SysNoticePush> dataList) {

    List<String> userIds = new ArrayList<>();
    dataList.stream().forEach(t -> {
      String userId = t.getUserId();
      if (StringUtils.isNotEmpty(userId)) {
        userIds.add(userId);
      }
    });
    return userIds;
  }

  @Override
  public Integer getWillReadCount(String userId) {
    String date =  DateFormatUtils.format( new Date(), DateTools.YYYY_MM_DD_HH_MM_SS);
    return StringUtils.isEmpty(userId) ? 0 : jpaRepository.getWillReadCount(userId,date);
  }

  @Override
  public SysNoticePush read(SysNoticeInfo noticeInfo) {
    if (null == noticeInfo)
      throw new BizException("消息/公告数据为空");
    LoginUser loginUser = dataManager.getLoginUser();
    String userName = loginUser.getUserName();
    String userId = loginUser.getUserId();
    UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);

    SysNoticePush push = jpaRepository.getByUserId(userId, noticeInfo.getNoticeId());
    if (null == push) {
      push = new SysNoticePush();
      push.setNoticeId(noticeInfo.getNoticeId());
      push.setUserId(loginUser.getUserId());
      push.setDeptId(userInfo.getDeptId());
      push.setPushTime(LocalDateTime.now());
    }
    push.setIsPush(UseStatus.NO.getData());
    Integer readCount = null == push.getReadCount() ? 1 : push.getReadCount() + 1;
    push.setReadCount(readCount);
    push.setIsRead(UseStatus.YES.getData());
    push.setLcu(userName);
    push.setLcd(LocalDateTime.now());
    super.store(push);

    return push;
  }

  @Override
  public List<SysNoticePush> findInId(List<String> idList) {
    return CollectionUtils.isEmpty(idList) ? null : jpaRepository.findAllById(idList);
  }
}
