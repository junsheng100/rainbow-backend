package com.rainbow.template.resource.impl;

import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.template.entity.TemplateResult;
import com.rainbow.template.repository.TemplateResultRepository;
import com.rainbow.template.resource.TemplateResultDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemplateResultDaoImpl extends BaseDaoImpl<TemplateResult, String, TemplateResultRepository>  implements TemplateResultDao {
}
