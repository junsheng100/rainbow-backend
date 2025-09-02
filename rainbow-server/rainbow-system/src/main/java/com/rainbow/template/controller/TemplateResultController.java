package com.rainbow.template.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.template.entity.TemplateResult;
import com.rainbow.template.service.TemplateResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template/result")
@Tag(name = "模板/数据")
public class TemplateResultController extends BaseController<TemplateResult, String, TemplateResultService> {



}
