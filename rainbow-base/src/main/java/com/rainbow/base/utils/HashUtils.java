package com.rainbow.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

@Slf4j
public class HashUtils {

  // 支持的哈希算法
  public enum HashAlgorithm {
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private final String algorithmName;

    HashAlgorithm(String algorithmName) {
      this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
      return algorithmName;
    }
  }


  /**
   * 计算文件哈希值
   */
  public static String hash(File file, HashAlgorithm algorithm) {
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithmName());

      try (FileInputStream fis = new FileInputStream(file)) {
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
          md.update(buffer, 0, bytesRead);
        }
      }

      byte[] hashBytes = md.digest();
      return bytesToHex(hashBytes);
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