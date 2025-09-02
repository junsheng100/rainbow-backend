package com.rainbow.system.resource.impl;

import com.rainbow.base.exception.DataException;
import com.rainbow.base.resource.impl.BaseDaoImpl;
import com.rainbow.system.entity.FileType;
import com.rainbow.system.repository.FileTypeRepository;
import com.rainbow.system.resource.FileTypeDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class FileTypeDaoImpl extends BaseDaoImpl<FileType, String,FileTypeRepository> implements FileTypeDao {


  public FileType findMimeTypeAndExtension(String mimiType, String extension) {
    if (StringUtils.isBlank(extension))
      Assert.notNull(extension, "扩展名称不能为空");
    if (StringUtils.isBlank(mimiType))
      Assert.notNull(mimiType, "mime-type 不能为空");
    return super.jpaRepository.findMimeTypeAndExtension(mimiType, extension);
  }

  @Override
  public List<String> findAllow() {
    return super.jpaRepository.findAllow();
  }

  @Override
  public List<FileType> findByMimeType(String mimeType) {
    if(StringUtils.isBlank(mimeType))
      throw new DataException("Mime-Type is blank");
    return super.jpaRepository.findByMimeType(mimeType);
  }

  @Override
  public FileType findByTypName(String fileExt) {
    return StringUtils.isBlank(fileExt) ? null : super.jpaRepository.findByTypName(fileExt);
  }

  @Override
  public List<FileType> findByTypNameIn(List<String> fileExtList) {
    return CollectionUtils.isEmpty(fileExtList)? Collections.emptyList(): super.jpaRepository.findByTypNameIn(fileExtList);
  }

  @Override
  public List<FileType> findByTyExtensionIn(List<String> fileExtList) {
    return  CollectionUtils.isEmpty(fileExtList)?null:jpaRepository.findByTyExtensionIn(fileExtList);
  }

  @Override
  public FileType findByExtension(String fileExt) {
    return StringUtils.isBlank(fileExt)?null:jpaRepository.findByExtension(fileExt);
  }


}
