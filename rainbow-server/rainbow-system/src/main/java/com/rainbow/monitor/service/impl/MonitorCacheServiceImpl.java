package com.rainbow.monitor.service.impl;

import com.rainbow.base.utils.StringUtils;
import com.rainbow.monitor.model.dto.CacheTree;
import com.rainbow.monitor.service.MonitorCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MonitorCacheServiceImpl implements MonitorCacheService {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Override
  public Map<String, Object> getInfo() {

    Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
    Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
    Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

    Map<String, Object> map = new HashMap<>(3);
    map.put("info", info);
    map.put("dbSize", dbSize);

    List<Map<String, String>> pieList = new ArrayList<>();
    commandStats.stringPropertyNames().forEach(key -> {
      Map<String, String> data = new HashMap<>(2);
      String property = commandStats.getProperty(key);
      data.put("name", StringUtils.removeStart(key, "cmdstat_"));
      data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
      pieList.add(data);
    });
    map.put("commandStats", pieList);
    return map;

  }

  @Override
  public List<CacheTree> getTreeInfo() {
    List<CacheTree> list = new ArrayList<>();
    Set<String> allKeys = redisTemplate.keys("*");
    List<String> keysList = new ArrayList<>(allKeys);
    Collections.sort(keysList);
    int i = 1;
    for (String key : keysList) {
      CacheTree tree = new CacheTree();
      tree.setOrderNo(i++);
      tree.setParentId("");

      if (key.indexOf(":") != -1) {
        String[] split = key.split(":");
//        tree.setLabel(split[0]);
//        tree.setId(split[0]);
//        List<CacheTree> children = new ArrayList<>();
//        getCacheChildren(tree, key, children);
        convertTree(tree, split);

      } else {
        tree.setLabel(key);
        tree.setId(key);
        tree.setLabel(key);
      }


      list.add(tree);
    }


    return list;
  }

  private void convertTree(CacheTree tree, String[] split) {

  }

  private void getCacheChildren(CacheTree tree, String key, List<CacheTree> children) {


  }
}
