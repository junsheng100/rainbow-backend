package com.rainbow.user.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.UserPost;
import com.rainbow.user.resource.UserPostDao;
import com.rainbow.user.service.UserPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPostServiceImpl extends BaseServiceImpl<UserPost,Long, UserPostDao> implements UserPostService {
}
