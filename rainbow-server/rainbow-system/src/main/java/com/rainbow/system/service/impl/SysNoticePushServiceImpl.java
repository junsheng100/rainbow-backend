package com.rainbow.system.service.impl;

import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.SysNoticeInfo;
import com.rainbow.system.entity.SysNoticePush;
import com.rainbow.system.enums.PushTimeType;
import com.rainbow.system.model.dto.NoticePushDto;
import com.rainbow.system.resource.SysNoticeInfoDao;
import com.rainbow.system.resource.SysNoticePushDao;
import com.rainbow.system.service.SysNoticePushService;
import com.rainbow.user.entity.DeptInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.resource.DeptInfoDao;
import com.rainbow.user.resource.UserInfoDao;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysNoticePushServiceImpl extends BaseServiceImpl<SysNoticePush, String, SysNoticePushDao> implements SysNoticePushService {

  @Autowired
  private SysNoticeInfoDao infoDao;
  @Autowired
  private DeptInfoDao deptInfoDao;
  @Autowired
  private UserInfoDao userDao;


  @Override
  public void parameters(BaseVo<SysNoticePush> vo) {
    try {
      LoginUser user = baseDao.getLoginUser();
      SysNoticePush entity = vo.getData();
      if (!super.isAdmin()) {
        entity.setUserId(user.getUserId());
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new BizException(e.getMessage());
    }
  }


  @Transactional
  @Override
  public Boolean savePlanData(NoticePushDto vo) {
    // 校验是否存在通知公告
    if (vo.getNoticeId() == null || infoDao.get(vo.getNoticeId()) == null) {
      throw new IllegalArgumentException("通知公告不存在");
    }

    PushTimeType timeType = vo.getPushTimeType();

    if (PushTimeType.AFTER.equals(timeType)) {
      // 校验推送时间类型不能为空
      // 校验推送时间不能为空
      if (vo.getPushTime() == null) {
        throw new IllegalArgumentException("推送时间不能为空");
      }
      // 校验推送时间不能早于当前时间
      if (vo.getPushTime().isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("推送时间不能早于当前时间");
      }
    } else {
      vo.setPushTime(LocalDateTime.now());
    }

    Integer pushType = vo.getPushType();

    if (pushType == null) {
      throw new IllegalArgumentException("推送类型不能为空");
    }

    if (CollectionUtils.isEmpty(vo.getUserIds())) {
      List<String> userIds = deptInfoDao.findUserIdByDeptId(pushType, vo.getDeptIds());
      vo.setUserIds(userIds);
    }

    baseDao.savePushData(vo);

    return true;
  }

  @SneakyThrows
  @Override
  public Integer getWillReadCount() {

    LoginUser user = getLoginUser();
    Integer total  = baseDao.getWillReadCount(user.getUserId());
    return total==null?0:total;
  }


  @Override
  public Boolean delete(String id) {
    SysNoticePush data = baseDao.get(id);
    if (null == data)
      throw new IllegalArgumentException("推送数据不存在");
    if (data.getIsRead() > 0)
      throw new IllegalArgumentException("已读数据不能删除");
    return baseDao.remove(id);
  }


  @Transactional
  @Override
  public Boolean deleteInBatch(List<String> data) {

    if (CollectionUtils.isEmpty(data))
      throw new IllegalArgumentException("推送数据不能为空");
    List<SysNoticePush> list = baseDao.findInId(data);

    Long readCount = list.stream().filter(item -> item.getIsRead() > 0).count();
    if (readCount > 0)
      throw new IllegalArgumentException("已读数据不能删除");

    return baseDao.removeInBatch(data);
  }


  public void convertData(SysNoticePush entity) {
    if (null != entity) {
      Long noticeId = entity.getNoticeId();
      SysNoticeInfo notice = null == noticeId ? null : infoDao.get(entity.getNoticeId());
      entity.setNoticeTitle(null == notice ? null : notice.getNoticeTitle());

      Long deptId = entity.getDeptId();
      DeptInfo dept = null == deptId ? null : deptInfoDao.get(entity.getDeptId());
      entity.setDeptName(null == dept ? null : dept.getDeptName());

      String userId = entity.getUserId();
      UserInfo user = null == userId ? null : userDao.get(entity.getUserId());
      entity.setNickname(null == user ? null : user.getNickname());
    }

  }

  public void convertCollection(List<SysNoticePush> list) {

    List<Long> infoIdList = list.stream().map(SysNoticePush::getNoticeId).collect(Collectors.toList());
    List<Long> deptIdList = list.stream().map(SysNoticePush::getDeptId).collect(Collectors.toList());
    List<String> userIdList = list.stream().map(SysNoticePush::getUserId).collect(Collectors.toList());


    Map<Long, SysNoticeInfo> infoMap = infoDao.findMapInId(infoIdList, SysNoticeInfo.class);
    Map<Long, DeptInfo> deptMap = deptInfoDao.findMapInId(deptIdList, DeptInfo.class);
    Map<String, UserInfo> userMap = userDao.findMapInId(userIdList, UserInfo.class);

    list.forEach(item -> {
      SysNoticeInfo info = infoMap.get(item.getNoticeId());
      item.setNoticeTitle(null == info ? null : info.getNoticeTitle());

      DeptInfo dept = deptMap.get(item.getDeptId());
      item.setDeptName(null == dept ? null : dept.getDeptName());

      UserInfo user = userMap.get(item.getUserId());
      item.setNickname(null == user ? null : user.getNickname());

    });
  }
}
