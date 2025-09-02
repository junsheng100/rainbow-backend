package com.rainbow.user.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.user.entity.PostInfo;
import com.rainbow.user.resource.PostInfoDao;
import com.rainbow.user.service.PostInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostInfoServiceImpl extends BaseServiceImpl<PostInfo,Long, PostInfoDao> implements PostInfoService {
}
