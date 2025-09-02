package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.SysNoticePush;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysNoticePushRepository extends BaseRepository<SysNoticePush, String> {

  @Query("select t from SysNoticePush t where t.noticeId = ?1")
  List<SysNoticePush> findByNoticeId(Long noticeId);

  @Query("select t from SysNoticePush t where t.noticeId in (?1) ")
  List<SysNoticePush> findInNoticeId(List<Long> noticeIdList);

  @Query("select count(t) from SysNoticePush t where   t.isRead = 0  and t.userId  = ?1  and t.pushTime <= date_format(?2,'%Y-%m-%d %H:%i:%s') ")
  Integer getWillReadCount(String userId, String dateTime);

  @Query("select t from SysNoticePush t where  t.userId  = ?1 and t.noticeId = ?2  ")
  SysNoticePush getByUserId(String userId, Long noticeId);


}
