package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysFeedback;
import com.rainbow.system.model.vo.ReplyVo;


public interface SysFeedbackService extends BaseService<SysFeedback,String> {


  SysFeedback reply(ReplyVo reply);
}
