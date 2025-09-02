package com.rainbow.user.controller;

import com.rainbow.base.controller.BaseController;
import com.rainbow.user.entity.PostInfo;
import com.rainbow.user.service.PostInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/info")
@Tag(name = "岗位管理")
public class PostInfoController extends BaseController<PostInfo,Long, PostInfoService> {

}
