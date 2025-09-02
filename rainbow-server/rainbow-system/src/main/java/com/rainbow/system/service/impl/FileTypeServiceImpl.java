package com.rainbow.system.service.impl;

import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.system.entity.FileType;
import com.rainbow.system.resource.FileTypeDao;
import com.rainbow.system.resource.SysConfigDao;
import com.rainbow.system.service.FileTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FileTypeServiceImpl extends BaseServiceImpl<FileType,String, FileTypeDao> implements FileTypeService {




  @Autowired
  private SysConfigDao configDao;

  @Override
  public int uploadMimeType(MultipartFile multipartFile) {
    int size = 0;
    //创建一个XML解析器
    SAXReader reader = new SAXReader();
    try {
      String filePath = System.getProperty("user.home")+"/upload/temp/"+ DateFormatUtils.format(new Date(),"yyyy/MM/dd");
      File fdir = new File(filePath);
      if(!fdir.isDirectory())
        fdir.mkdirs();
      String fileName = multipartFile.getOriginalFilename();
      File file = new File(filePath+"/"+fileName);
      multipartFile.transferTo(file);
      Document doc = reader.read(file);
      assert doc != null;
      Element root = doc.getRootElement();
      List<Element> elementList = root.elements("mime-mapping");

      for (Element element : elementList) {
        size++;
        String extension = element.elementText("extension");
        String mimeType = element.elementText("mime-type");

        FileType data = new FileType(extension,mimeType);
        data.setApprove(0);
        data.setRefuse(0);
        data.setTypeName(extension);
        super.store(data);
      }

    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return size;
  }

  @Override
  public List<String> findAllow() {
    return super.baseDao.findAllow();
  }

  @Override
  public String uploadFile(MultipartFile multipartFile, String fileName) {
    FileType data = null;
    File file = null;
    try {

      String filePath = configDao.getFileBasePath() + fileName;
      file = new File(filePath);

      if(file.exists())
        return fileName;
      File fdir = file.getParentFile();
      if(!fdir.exists())
        fdir.mkdirs();

      multipartFile.transferTo(file);
      return fileName;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }

}
