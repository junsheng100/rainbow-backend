package com.rainbow.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class ShaUtils {

  public static String hash(String input) {
    try {
      // 获取 SHA-1 实例
      MessageDigest md = MessageDigest.getInstance("SHA-1");

      // 计算哈希值
      byte[] hashBytes = md.digest(input.getBytes());

      // 将字节数组转换为十六进制字符串
      StringBuilder hexString = new StringBuilder();
      for (byte b : hashBytes) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-1 algorithm not found", e);
    }
  }

  public static String hash(File file) {
    try {
      if (!file.exists() || !file.isFile())
        throw new IOException("File does not exist: " + file.getPath());

      MessageDigest md = MessageDigest.getInstance("SHA-1");

      try (FileInputStream fis = new FileInputStream(file)) {
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
          md.update(buffer, 0, bytesRead);
        }

        byte[] hashBytes = md.digest();
        return bytesToHex(hashBytes);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }


  public static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

}
