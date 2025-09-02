package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.SysResource;
import org.springframework.web.multipart.MultipartFile;

public interface SysResourceService extends BaseService<SysResource,Long> {


  String uploadFile(MultipartFile multipartFile);


  SysResource uploadFile(MultipartFile multipartFile,String filePath);
}
