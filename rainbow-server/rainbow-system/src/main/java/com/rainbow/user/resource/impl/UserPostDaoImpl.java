package com.rainbow.user.resource.impl;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.user.entity.PostInfo;
import com.rainbow.user.entity.UserInfo;
import com.rainbow.user.entity.UserPost;
import com.rainbow.user.repository.PostInfoRepository;
import com.rainbow.user.repository.UserPostRepository;
import com.rainbow.user.resource.UserPostDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserPostDaoImpl extends BaseDaoImpl<UserPost,Long, UserPostRepository> implements UserPostDao {

  @Autowired
  private PostInfoRepository postInfoRepository;

  @Override
  public String getPostName(String userId) {
    if (StringUtils.isBlank(userId))
      return "";
    List<Long> postIdList = jpaRepository.findPostIdByUserId(userId);

    if (CollectionUtils.isNotEmpty(postIdList)) {
      List<PostInfo> postInfoList = postInfoRepository.findAllById(postIdList);
      String postName = CollectionUtils.isEmpty(postInfoList) ? "" : postInfoList.stream().map(PostInfo::getPostName).collect(Collectors.joining(ChartEnum.COMMA.getCode()));
      return postName;
    }
    return "";
  }

  @Override
  public Map<String, List<UserPost>> findUserPostList(List<String> userIdList) {
    Map<String, List<UserPost>> mp = new HashMap<>();

    if (CollectionUtils.isNotEmpty(userIdList)) {
      List<UserPost> postList = jpaRepository.findInUserId(userIdList);
      if (CollectionUtils.isNotEmpty(postList)) {
        List<Long> postIdList = postList.stream().map(UserPost::getPostId).collect(Collectors.toList());
        List<PostInfo> postInfoList = CollectionUtils.isEmpty(postIdList) ? null : postInfoRepository.findAllById(postIdList);
        postList.stream().forEach(ps -> {
          String postName = postInfoList.stream().filter(t -> ps.getPostId().equals(t.getPostId())).map(PostInfo::getPostName).distinct().collect(Collectors.joining(ChartEnum.COMMA.getCode()));
          ps.setPostName(postName);
        });
        mp = postList.stream().collect(Collectors.groupingBy(UserPost::getUserId));
      }

    }
    return mp;
  }

  @Override
  public void storeUserInfo(UserInfo userInfo) {
    if (null == userInfo)
      return;
    if (StringUtils.isBlank(userInfo.getUserId()))
      return;
    if (CollectionUtils.isEmpty(userInfo.getPostIdList()))
      return;
    String userId = userInfo.getUserId();
    List<Long> postIdList = userInfo.getPostIdList();
    List<UserPost> oldList = jpaRepository.findByUserId(userId);

    if (CollectionUtils.isNotEmpty(oldList))
      jpaRepository.deleteAll(oldList);

    String userName = super.getUserName();
    LocalDateTime date = LocalDateTime.now();

    for(Long postId:postIdList){
      UserPost userPost = new UserPost(userId,postId,userName,date,userId,date, UseStatus.NO.getCode());
      jpaRepository.save(userPost);
    }
  }
}
