package com.rainbow.system.utils;

import com.rainbow.system.entity.SysConfig;
import com.rainbow.system.resource.SysConfigDao;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class WallPaperFileStorageUtil {


  String user_home = "${user.home}";
  String user_dir = "${user.dir}";

  @Autowired
  private SysConfigDao configDao;


  private Path wallpaperPath;
  private Path fullImagePath;
  private Path previewImagePath;
  private Path thumbImagePath;

  @PostConstruct
  public void init() {
    try {

      String basePath = getBasePath();

      wallpaperPath = Paths.get(basePath);
      fullImagePath = wallpaperPath.resolve("images/full");
      previewImagePath = wallpaperPath.resolve("images/preview");
      thumbImagePath = wallpaperPath.resolve("images/thumb");

      // 创建目录
      Files.createDirectories(wallpaperPath);
      Files.createDirectories(fullImagePath);
      Files.createDirectories(previewImagePath);
      Files.createDirectories(thumbImagePath);

      log.info("Wallpaper storage initialized. Base path: {}", wallpaperPath.toAbsolutePath());
    } catch (IOException e) {
      log.error("Failed to initialize wallpaper storage", e);
      throw new RuntimeException("Failed to initialize wallpaper storage", e);
    }
  }

  public String getBasePath() {
    String basePath =  configDao.getFileBasePath();

    if(basePath.startsWith(user_home)){
      basePath = basePath.replace(user_home, System.getProperty(user_home) );
    }
    if(basePath.startsWith(user_dir)){
      basePath = basePath.replace(user_dir, System.getProperty(user_dir) );
    }
    return basePath;
  }

  /**
   * 保存图片文件
   */
  public String saveImage(byte[] imageData, String startDate) throws IOException {
    if (imageData == null || imageData.length == 0) {
      throw new IllegalArgumentException("Image data cannot be null or empty");
    }
    SysConfig config = configDao.findByKey("MAX_FILE_SIZE");
    Long maxFileSize = null == config ? 1024 * 1024 * 10 : Long.valueOf(config.getConfigValue());
    if (imageData.length > maxFileSize) {
      throw new IllegalArgumentException("Image size exceeds maximum allowed size: " + maxFileSize);
    }

    String fileName = generateFileName(startDate, "jpg");
    Path targetFile = fullImagePath.resolve(fileName);

    Files.write(targetFile, imageData);

    String relativePath = "images/full/" + fileName;
    log.info("Image saved successfully: {}", relativePath);

    return relativePath;
  }

  /**
   * 保存预览图
   */
  public String savePreviewImage(byte[] imageData, String startDate) throws IOException {
    if (imageData == null || imageData.length == 0) {
      throw new IllegalArgumentException("Image data cannot be null or empty");
    }

    String fileName = generateFileName(startDate, "jpg");
    Path targetFile = previewImagePath.resolve(fileName);

    Files.write(targetFile, imageData);

    String relativePath = "images/preview/" + fileName;
    log.info("Preview image saved successfully: {}", relativePath);

    return relativePath;
  }

  /**
   * 删除图片文件
   */
  public boolean deleteImage(String relativePath) {
    if (!StringUtils.hasText(relativePath)) {
      return false;
    }

    try {
      Path filePath = wallpaperPath.resolve(relativePath);
      if (Files.exists(filePath)) {
        Files.delete(filePath);
        log.info("Image deleted successfully: {}", relativePath);
        return true;
      } else {
        log.warn("Image file not found: {}", relativePath);
        return false;
      }
    } catch (IOException e) {
      log.error("Failed to delete image: {}", relativePath, e);
      return false;
    }
  }

  /**
   * 获取图片的绝对路径
   */
  public Path getImagePath(String relativePath) {
    if (!StringUtils.hasText(relativePath)) {
      return null;
    }
    return wallpaperPath.resolve(relativePath);
  }

  /**
   * 获取图片的访问URL
   */
  public String getImageUrl(String relativePath) {
    if (!StringUtils.hasText(relativePath)) {
      return null;
    }
    SysConfig config = configDao.findByKey("WALL_PAGE_URL_PREFIX");
    String urlPrefix = null != config?config.getConfigValue():"/api/wallpaper/images";
    return urlPrefix + "/" + relativePath;
  }

  /**
   * 检查文件是否存在
   */
  public boolean exists(String relativePath) {
    if (!StringUtils.hasText(relativePath)) {
      return false;
    }
    Path filePath = wallpaperPath.resolve(relativePath);
    return Files.exists(filePath);
  }

  /**
   * 获取文件大小
   */
  public long getFileSize(String relativePath) {
    if (!StringUtils.hasText(relativePath)) {
      return 0;
    }

    try {
      Path filePath = wallpaperPath.resolve(relativePath);
      if (Files.exists(filePath)) {
        return Files.size(filePath);
      }
    } catch (IOException e) {
      log.error("Failed to get file size: {}", relativePath, e);
    }

    return 0;
  }

  /**
   * 移动文件
   */
  public boolean moveFile(String sourcePath, String targetPath) {
    try {
      Path source = wallpaperPath.resolve(sourcePath);
      Path target = wallpaperPath.resolve(targetPath);

      // 确保目标目录存在
      Files.createDirectories(target.getParent());

      Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
      log.info("File moved from {} to {}", sourcePath, targetPath);
      return true;
    } catch (IOException e) {
      log.error("Failed to move file from {} to {}", sourcePath, targetPath, e);
      return false;
    }
  }

  /**
   * 生成文件名
   */
  public String generateFileName(String startDate, String extension) {
    if (!StringUtils.hasText(startDate)) {
      startDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }
    return startDate + "." + extension;
  }

  /**
   * 清理旧文件
   */
  public int cleanupOldFiles(int daysToKeep) {
    LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);
    String cutoffDateStr = cutoffDate.format(DateTimeFormatter.BASIC_ISO_DATE);

    int deletedCount = 0;

    try {
      // 清理 full 目录
      deletedCount += cleanupDirectory(fullImagePath, cutoffDateStr);
      // 清理 preview 目录
      deletedCount += cleanupDirectory(previewImagePath, cutoffDateStr);
      // 清理 thumb 目录
      deletedCount += cleanupDirectory(thumbImagePath, cutoffDateStr);

      log.info("Cleanup completed. Deleted {} files older than {} days", deletedCount, daysToKeep);
    } catch (Exception e) {
      log.error("Error during cleanup", e);
    }

    return deletedCount;
  }

  /**
   * 清理指定目录的旧文件
   */
  private int cleanupDirectory(Path directory, String cutoffDateStr) throws IOException {
    if (!Files.exists(directory)) {
      return 0;
    }

    int deletedCount = 0;

    Files.list(directory)
            .filter(Files::isRegularFile)
            .filter(file -> {
              String fileName = file.getFileName().toString();
              String dateStr = fileName.substring(0, Math.min(8, fileName.length()));
              return dateStr.compareTo(cutoffDateStr) < 0;
            })
            .forEach(file -> {
              try {
                Files.delete(file);
                log.debug("Deleted old file: {}", file.getFileName());
              } catch (IOException e) {
                log.warn("Failed to delete file: {}", file.getFileName(), e);
              }
            });

    return deletedCount;
  }

  public String getImageCode(String imagePath) {

    try {

      File file = new File(imagePath);
      String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1);
      FileInputStream imageInFile = new FileInputStream(file);
      byte[] imageData = new byte[(int) file.length()];
      imageInFile.read(imageData);
      String imageInBase64 = "data:image/" + suffix + ";base64," + Base64.encodeBase64String(imageData);

      imageInFile.close();
      return imageInBase64;
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return null;
    }

  }
} 