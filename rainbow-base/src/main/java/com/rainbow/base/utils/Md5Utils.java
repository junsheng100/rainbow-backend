package com.rainbow.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密方法
 *
 * @author rainvom
 */
@Slf4j
public class Md5Utils {


  public static String hash(String s) {
    return DigestUtils.md5Hex(s);
  }

  /**
   * 获取上传文件的md5
   *
   * @param file 上传文件
   */
  public static String getMultipartFileMd5(MultipartFile file) {
    try {
      byte[] uploadBytes = file.getBytes();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] digest = md5.digest(uploadBytes);
      String hashString = new BigInteger(1, digest).toString(16);
      return hashString;
    } catch (Exception e) {
      log.error("获取上传文件的md5异常",e);
    }
    return null;
  }


  /**
   * 获取一个文件的md5值(可处理大文件)
   * @return md5 value
   */
  public static String getMD5(File file) {
    FileInputStream fileInputStream = null;
    try {
      MessageDigest MD5 = MessageDigest.getInstance("MD5");
      fileInputStream = new FileInputStream(file);
      byte[] buffer = new byte[8192];
      int length;
      while ((length = fileInputStream.read(buffer)) != -1) {
        MD5.update(buffer, 0, length);
      }
      return new String(Hex.encodeHex(MD5.digest()));
    } catch (Exception e) {
      log.error("获取一个文件的md5值(可处理大文件)异常",e);
      return null;
    } finally {
      try {
        if (fileInputStream != null){
          fileInputStream.close();
        }
      } catch (IOException e) {
        log.error("文件流关闭异常",e);
      }
    }
  }


  public static String getCode(String text ) {
    // 1. 获取 MD5 哈希实例
    MessageDigest md = null;
    String md5Hash = "";
    try {
      md = MessageDigest.getInstance("MD5");
      // 2. 计算哈希值（返回 byte[]）
      byte[] hashBytes = md.digest(text.getBytes());

      // 3. 将 byte[] 转换为 16 进制字符串
      BigInteger bigInt = new BigInteger(1, hashBytes);
      md5Hash = bigInt.toString(16);

      // 补零（确保 32 位长度）
      while (md5Hash.length() < 32) {
        md5Hash = "0" + md5Hash;
      }
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    return md5Hash;
  }

}
