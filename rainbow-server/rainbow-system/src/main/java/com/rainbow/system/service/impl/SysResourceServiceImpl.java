package com.rainbow.system.service.impl;

import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.Md5Utils;
import com.rainbow.base.utils.RandomId;
import com.rainbow.system.entity.FileType;
import com.rainbow.system.entity.SysResource;
import com.rainbow.system.resource.FileTypeDao;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.system.resource.SysResourceDao;
import com.rainbow.system.service.SysResourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource, Long, SysResourceDao> implements SysResourceService {

  @Autowired
  private FileTypeDao typeDao;

  @Autowired
  private SysConfigDao configDao;


  @SneakyThrows
  @Override
  public String uploadFile(MultipartFile multipartFile) {
    String srcName = multipartFile.getOriginalFilename();
    String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
    String fileUrl = DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + RandomId.generateShortUuid(12) + "." + suffix;

    uploadFile(multipartFile, fileUrl);

    return fileUrl;
  }

  @SneakyThrows
  @Override
  public SysResource uploadFile(MultipartFile multipartFile, String fileName) {
    SysResource data = null;
    File file = null;
    try {
      boolean isAllow = isAllowFile(multipartFile);

      if (!isAllow)
        throw new BizException("文件格式不支持");

      String filePath = configDao.getFileBasePath() + File.separator + fileName;
      filePath = filePath.replace("//", "/");
      file = new File(filePath);
      File fdir = file.getParentFile();
      if (!fdir.exists())
        fdir.mkdirs();

      String srcName = multipartFile.getOriginalFilename();
      String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);

      multipartFile.transferTo(file);

      String md5Code = Md5Utils.getMD5(file);
      data = new SysResource(multipartFile);
      data.setFileUrl(fileName);
      data.setMd5Code(md5Code);
      data.setFileExt(suffix);
      data.setStatus(UseStatus.NO.getCode());

      super.baseDao.store(data);

    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      if (null != file)
        file.delete();
      throw e;
    }

    return data;
  }

  private boolean isAllowFile(MultipartFile multipartFile) {

    List<String> allow = typeDao.findAllow();
    if (CollectionUtils.isNotEmpty(allow)) {
      String fileExt = multipartFile.getOriginalFilename();
      fileExt = fileExt.substring(fileExt.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
      return allow.contains(fileExt);
    }
    return false;
  }

  @Override
  public Boolean delete(Long id) {
    return baseDao.remove(id);
  }


  public void convertData(SysResource entity) {
    if (null != entity) {
      String fileExt = entity.getFileExt();
      FileType type = StringUtils.isBlank(fileExt) ? null : typeDao.findByExtension(fileExt);
      if (null != type) {
        entity.setLogo(type.getLogo());
      }
    }
  }

  public void convertCollection(List<SysResource> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      List<String> fileExtList = list.stream().map(SysResource::getFileExt).distinct().collect(Collectors.toList());
      List<FileType> typeList = typeDao.findByTyExtensionIn(fileExtList);
      if (CollectionUtils.isNotEmpty(typeList)) {
        Map<String, FileType> typeMap = typeList.stream().collect(Collectors.toMap(FileType::getExtension, item -> item));
        list.forEach(item -> {
          String fileExt = item.getFileExt();
          if (StringUtils.isNotBlank(fileExt)) {
            FileType type = typeMap.get(fileExt);
            if (null != type) {
              item.setLogo(type.getLogo());
            }
          }
        });
      }
    }
  }
}
