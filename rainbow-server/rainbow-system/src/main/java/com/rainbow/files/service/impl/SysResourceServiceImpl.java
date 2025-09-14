package com.rainbow.files.service.impl;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.exception.BizException;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.Md5Utils;
import com.rainbow.base.utils.RandomId;
import com.rainbow.files.config.ResourceConfig;
import com.rainbow.files.entity.FileType;
import com.rainbow.files.entity.SysResource;
import com.rainbow.files.resource.FileTypeDao;
import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.files.resource.SysResourceDao;
import com.rainbow.files.service.SysResourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource, String, SysResourceDao> implements SysResourceService {

  @Autowired
  private FileTypeDao typeDao;

  @Autowired
  private SysConfigDao configDao;

  public static final String FILE_CONFIG = "file_config";


  @SneakyThrows
  @Override
  public String uploadFile(MultipartFile multipartFile) {
    String srcName = multipartFile.getOriginalFilename();
    String suffix = srcName.substring(srcName.lastIndexOf(ChartEnum.POINT.getCode()) + 1);
    String fileUrl = DateFormatUtils.format(new Date(), "yyyy/MM/dd") + "/" + RandomId.generateShortUuid(12) + "." + suffix;

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

      file.setWritable(true);
      file.setReadable(true);
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

  @Override
  public ResourceConfig getResourceConfig() {
    SysConfig sysConfig = configDao.findByKey(FILE_CONFIG);
    if (null != sysConfig) {
      String value = sysConfig.getConfigValue();
      if (com.rainbow.base.utils.StringUtils.isNotBlank(value)) {
        ResourceConfig config = JSON.parseObject(value, ResourceConfig.class);
        return config;
      }
    }
    return null;
  }

  @Override
  public ResourceConfig setUploadConfig(ResourceConfig config) {

    SysConfig sysConfig = new SysConfig();

    sysConfig.setConfigName("系统资源管理");
    sysConfig.setConfigKey(FILE_CONFIG);
    String value = JSON.toJSONString(config);
    sysConfig.setConfigValue(value);
    sysConfig.setStatus("0");

    configDao.store(sysConfig);

    return config;
  }

  @Override
  public boolean validate(MultipartFile multipartFile) {
    if (null == multipartFile)
      throw new BizException("file is null");
    ResourceConfig config = getResourceConfig();
    if (null == config)
      return true;

    Long size = multipartFile.getSize();
    String fileExt = multipartFile.getOriginalFilename();
    fileExt = fileExt.substring(fileExt.lastIndexOf(ChartEnum.POINT.getCode()) + 1);

    Long maxSize = config.getMaxSize() * 1024L * 1024L;
    String[] allowExtensions = config.getAllowExtensions();
    List<String> allowList = null == allowExtensions || allowExtensions.length == 0 ? null : Arrays.asList(allowExtensions);

    if (maxSize < size)
      throw new BizException("文件大小超出限制");

    if (CollectionUtils.isEmpty(allowList))
      return true;

    if (allowList.contains(fileExt))
      return true;

    throw new BizException("文件格式不支持");
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
  public Boolean delete(String id) {
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
