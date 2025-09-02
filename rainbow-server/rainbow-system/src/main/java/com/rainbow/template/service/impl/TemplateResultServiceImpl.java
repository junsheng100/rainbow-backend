package com.rainbow.template.service.impl;

import com.alibaba.fastjson2.JSON;
import com.rainbow.base.enums.ChartEnum;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.RandomId;
import com.rainbow.base.utils.StringUtils;
import com.rainbow.template.entity.TemplateConfig;
import com.rainbow.template.entity.TemplateEntity;
import com.rainbow.template.entity.TemplateField;
import com.rainbow.template.entity.TemplateResult;
import com.rainbow.template.resource.TemplateConfigDao;
import com.rainbow.template.resource.TemplateEntityDao;
import com.rainbow.template.resource.TemplateFieldDao;
import com.rainbow.template.resource.TemplateResultDao;
import com.rainbow.template.service.TemplateResultService;
import freemarker.template.Template;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateResultServiceImpl extends BaseServiceImpl<TemplateResult, String, TemplateResultDao> implements TemplateResultService {

  @Autowired
  private TemplateEntityDao templateEntityDao;

  @Autowired
  private TemplateFieldDao templateFieldDao;

  @Autowired
  private TemplateConfigDao templateConfigDao;


  @SneakyThrows
  public void handleData(@Valid TemplateResult result) {
    String configId = result.getConfigId();
    String entityId = result.getEntityId();
    String srcContent = result.getSrcContent();
    String packageName = result.getPackageName();
    TemplateConfig config = StringUtils.isBlank(configId) ? null : templateConfigDao.get(configId);
    TemplateEntity data = StringUtils.isBlank(entityId) ? null : templateEntityDao.get(entityId);

    srcContent = StringUtils.isBlank(srcContent) ? null == config ? "" : config.getContent() : srcContent;

    String idType = data.getIdType();
    String idShortType = data.getIdShortType();
    idShortType = StringUtils.isBlank(idShortType) ? idType.substring(idType.lastIndexOf(ChartEnum.POINT.getCode()) + 1) : idShortType;
    data.setIdShortType(idShortType);

    Map<String, Object> dataMap = new LinkedHashMap<>();
    Map<String, Object> entityMap = JSON.parseObject(JSON.toJSONString(data));
    Map<String, Object> resultMap = JSON.parseObject(JSON.toJSONString(result));

    dataMap.putAll(entityMap);
    dataMap.putAll(resultMap);

    String content = "";


    List<TemplateField> fieldList = templateFieldDao.findByEntityId(entityId);

    if (CollectionUtils.isNotEmpty(fieldList)) {
      dataMap.put("fieldList", fieldList);

      List<String> typeList = fieldList.stream().map(TemplateField::getFieldType).distinct().collect(Collectors.toList());
      dataMap.put("typeList", typeList);
    }



    StringReader reader = new StringReader(srcContent);

    Template template = new Template(config.getName(), reader);

    String filePath = System.getProperty("user.dir") + File.separator + "temp" + File.separator + RandomId.generateShortUuid() + "." + config.getSuffix();
    File file = new File(filePath);
    File fdir = file.getParentFile();
    if (!fdir.isDirectory())
      fdir.mkdirs();
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    template.process(dataMap, new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8").name()));
    fileOutputStream.close();

    content = readFileResult(file);

    String fileName = "";

    if (config.getIsEntity()) {
      fileName = data.getEntityName() + "." + config.getSuffix();
    } else {
      fileName = data.getEntityName() + (config.getIsEntity() ? "" : config.getName()) + "." + config.getSuffix();
    }

    result.setFileName(fileName);
    result.setContent(content);

  }


  @SneakyThrows
  private String readFileResult(File file) {
    String content = "";

    BufferedReader reader = null;
    try {
      if (null != file && file.exists()) {
        Path path = Paths.get(file.getPath());
        reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line;
        while ((line = reader.readLine()) != null) {
          content += line + "\n";
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {

      if (null != file) {
        file.delete();
        reader.close();
      }
    }
    return content;
  }


  public void convertData(TemplateResult entity) {
    String configId = entity.getConfigId();
    String entityId = entity.getEntityId();
    String srcContent = entity.getSrcContent();
    String packageaName = entity.getPackageName();
    TemplateConfig config = StringUtils.isBlank(configId) ? null : templateConfigDao.get(configId);
    TemplateEntity data = StringUtils.isBlank(entityId) ? null : templateEntityDao.get(entityId);
    if (StringUtils.isBlank(srcContent)) {
      srcContent = null == config ? "" : config.getContent();
      entity.setSrcContent(srcContent);
    }
    if (StringUtils.isBlank(packageaName)) {
      packageaName = null == data ? "" : data.getPackageName();
      entity.setPackageName(packageaName);
    }
  }

  public void convertCollection(List<TemplateResult> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      List<String> configIdList = list.stream().filter(t -> StringUtils.isNotBlank(t.getConfigId())).map(TemplateResult::getConfigId).distinct().collect(Collectors.toList());
      List<String> entityIdList = list.stream().filter(t -> StringUtils.isNotBlank(t.getEntityId())).map(TemplateResult::getEntityId).distinct().collect(Collectors.toList());
      Map<String, TemplateConfig> cfgMp = templateConfigDao.findMapInId(configIdList, TemplateConfig.class);
      Map<String, TemplateEntity> cfgEntity = templateEntityDao.findMapInId(entityIdList, TemplateEntity.class);

      list.stream().forEach(entity -> {
        String srcContent = entity.getSrcContent();
        if (StringUtils.isBlank(srcContent)) {
          TemplateConfig config = (TemplateConfig) MapUtils.getObject(cfgMp, entity.getConfigId(), null);
          srcContent = null == config ? "" : config.getContent();
          entity.setSrcContent(srcContent);
        }
        TemplateEntity data = (TemplateEntity) MapUtils.getObject(cfgEntity, entity.getEntityId(), null);
        if (StringUtils.isBlank(entity.getPackageName())) {
          String packageaName = null == data ? "" : data.getPackageName();
          entity.setPackageName(packageaName);
        }
      });
    }
  }


  @Override
  public Boolean delete(String id) {
    return baseDao.remove(id);
  }


  @Override
  public Boolean deleteInBatch(List<String> data) {
    return baseDao.removeInBatch(data);
  }


}
