package com.rainbow.files.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.files.config.ResourceConfig;
import com.rainbow.files.entity.SysResource;
import org.springframework.web.multipart.MultipartFile;

public interface SysResourceService extends BaseService<SysResource,String> {


  String uploadFile(MultipartFile multipartFile);


  SysResource uploadFile(MultipartFile multipartFile,String filePath);

  ResourceConfig getResourceConfig();

  ResourceConfig setUploadConfig(ResourceConfig config);

  boolean validate(MultipartFile multipartFile);
}
