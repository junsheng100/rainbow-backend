package com.rainbow.system.service.impl;

import com.rainbow.base.client.UserClient;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.model.domain.LoginUser;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.SysFeedback;
import com.rainbow.system.model.vo.ReplyVo;
import com.rainbow.system.resource.SysFeedbackDao;
import com.rainbow.system.service.SysFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SysFeedbackServiceImpl extends BaseServiceImpl<SysFeedback, String, SysFeedbackDao> implements SysFeedbackService {

  @Autowired
  private UserClient userClient;



  @Override
  public SysFeedback reply(ReplyVo replyVo)  {

    String id = replyVo.getId();
    Integer stage = replyVo.getStage();

    LoginUser loginUser = userClient.getLoginUser();
    String userName = loginUser.getUserName();
    SysFeedback entity = baseDao.get(id);
    if (entity == null) {
      throw new BizException("反馈信息不存在");
    }
    Assert.notNull(replyVo.getReply().trim(), "回复内容不能为空");
    LocalDateTime now = LocalDateTime.now();
    entity.setReply(replyVo.getReply().trim());
    entity.setReplyTime(now);
    entity.setReplyBy(userName);
    entity.setStage(stage);
    entity.setLcu(userName);
    entity.setLcd(now);

    super.baseDao.store(entity);

    return entity;
  }
}
