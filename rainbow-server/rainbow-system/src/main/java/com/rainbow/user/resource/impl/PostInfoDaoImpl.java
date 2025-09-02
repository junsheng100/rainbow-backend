package com.rainbow.user.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.user.entity.PostInfo;
import com.rainbow.user.repository.PostInfoRepository;
import com.rainbow.user.resource.PostInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostInfoDaoImpl extends BaseDaoImpl<PostInfo,Long, PostInfoRepository> implements PostInfoDao {
}
