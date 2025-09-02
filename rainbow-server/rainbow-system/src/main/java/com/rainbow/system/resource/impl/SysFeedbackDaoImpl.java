package com.rainbow.system.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.system.entity.SysFeedback;
import com.rainbow.system.repository.SysFeedbackRepository;
import com.rainbow.system.resource.SysFeedbackDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SysFeedbackDaoImpl extends BaseDaoImpl<SysFeedback,String, SysFeedbackRepository> implements SysFeedbackDao {

  @Override
  public SysFeedback check(SysFeedback entity) {
    SysFeedback old = getOne(entity);
    if (old == null) {
      String feedbackBy = entity.getFeedbackBy();
      if (StringUtils.isBlank(feedbackBy)) {
          entity.setFeedbackBy(getUserName());
      }

    }
    return old;
  }



}
