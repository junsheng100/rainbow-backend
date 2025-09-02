package com.rainbow.system.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.SysNoticeInfo;
import com.rainbow.system.entity.SysNoticePush;
import com.rainbow.system.model.dto.NoticePushDto;
import com.rainbow.user.entity.UserInfo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface SysNoticePushDao extends BaseDao<SysNoticePush, String> {

  boolean savePushData(@Valid NoticePushDto entity);

  void saveUserInfo(Long noticeId,SysNoticeInfo noticeInfo, List<UserInfo> userInfoList);

  NoticePushDto getPushData(Long noticeId);

  List<SysNoticePush> findInNoticeId(List<Long> noticeIdList);

  Map<Long, NoticePushDto> findPushData(List<SysNoticePush> list);

  Integer getWillReadCount(String userId);

  SysNoticePush read(SysNoticeInfo noticeInfo);

  List<SysNoticePush> findInId(List<String> idList);


}
