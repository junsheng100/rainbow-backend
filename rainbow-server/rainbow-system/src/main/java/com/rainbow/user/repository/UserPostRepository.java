package com.rainbow.user.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.user.entity.UserPost;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPostRepository extends BaseRepository<UserPost,Long> {


  @Query("select t from UserPost t where t.userId = ?1 ")
  List<UserPost> findByUserId(String userId);

  @Query("select t from UserPost t where t.userId in (?1) ")
  List<UserPost> findInUserId(List<String> userIdList);

  @Query("select t.postId from UserPost t where t.userId = ?1 ")
  List<Long> findPostIdByUserId(String userId);

  @Query("select t.postId from UserPost t where t.userId in (?1) ")
  List<Long> findPostIdInUserId(List<String> userIdList);


}
