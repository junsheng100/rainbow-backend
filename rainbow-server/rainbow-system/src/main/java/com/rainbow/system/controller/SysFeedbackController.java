package com.rainbow.system.controller;

import com.rainbow.base.annotation.NoRepeatSubmit;
import com.rainbow.base.annotation.OperLog;
import com.rainbow.base.controller.BaseController;
import com.rainbow.base.model.base.Result;
import com.rainbow.system.entity.SysFeedback;
import com.rainbow.system.model.vo.ReplyVo;
import com.rainbow.system.service.SysFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@Tag(name = "意见建议反馈")
public class SysFeedbackController extends BaseController<SysFeedback, String, SysFeedbackService> {

  @NoRepeatSubmit
  @OperLog("回复意见建议")
  @Operation(description = "回复意见建议")
  @PostMapping("/reply")
  public Result<SysFeedback> reply(@RequestBody ReplyVo vo) {
    return Result.success(service.reply(vo));
  }


}
