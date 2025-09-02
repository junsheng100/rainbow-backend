package com.rainbow.system.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.system.entity.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileTypeService extends BaseService<FileType,String> {


  int uploadMimeType(MultipartFile file);

  List<String> findAllow();

  String uploadFile(MultipartFile multipartFile, String localPath);
}
