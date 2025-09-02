package com.rainbow.system.resource;


import com.rainbow.base.resource.BaseDao;
import com.rainbow.system.entity.FileType;

import java.util.List;

public interface FileTypeDao extends BaseDao<FileType,String> {

  FileType findMimeTypeAndExtension(String mimiType, String extension);

  List<String> findAllow();

  List<FileType> findByMimeType(String mimeType);

  FileType findByTypName(String fileExt);

  List<FileType> findByTypNameIn(List<String> fileExtList);

  List<FileType> findByTyExtensionIn(List<String> fileExtList);

  FileType findByExtension(String fileExt);
}
