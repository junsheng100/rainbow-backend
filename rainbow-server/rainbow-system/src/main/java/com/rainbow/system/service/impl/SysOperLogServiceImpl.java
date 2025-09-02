package com.rainbow.system.service.impl;

import com.rainbow.base.enums.TimeType;
import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.model.vo.OperLogVo;
import com.rainbow.base.service.impl.BaseServiceImpl;
import com.rainbow.base.utils.CommonUtils;
import com.rainbow.base.utils.DateTools;
import com.rainbow.system.entity.SysIPData;
import com.rainbow.system.entity.SysOperLog;
import com.rainbow.system.model.vo.OperLogData;
import com.rainbow.system.model.vo.OperLogMonthData;
import com.rainbow.system.model.vo.OperLogUserData;
import com.rainbow.system.resource.SysIPDataDao;
import com.rainbow.system.resource.SysOperLogDao;
import com.rainbow.system.service.SysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysOperLogServiceImpl extends BaseServiceImpl<SysOperLog, Long, SysOperLogDao> implements SysOperLogService {

  @Autowired
  private SysIPDataDao ipAddressDao;

  @Override
  public Boolean receive(OperLogVo vo) {

    if (null == vo)
      return false;
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        saveReceive(vo);
      }
    };

    Thread thread = new Thread(runnable);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();

    return true;
  }

  @Override
  public Boolean cleanAll() {
    return super.baseDao.deleteAll();
  }

  @Override
  public List<OperLogData> totalOperTopList(Integer top, boolean desc) {
    List<OperLogData> list = new ArrayList<>();

    List<Object[]> topList = baseDao.totalOperTopList();

    if (CollectionUtils.isNotEmpty(topList)) {

      list = topList.stream().map(arr -> {
        String beanName = arr[0].toString();
        String method = arr[1].toString();
        String title = arr[2].toString();
        Long count = Long.valueOf(arr[3].toString());
        OperLogData logData = new OperLogData(beanName, method, title, count);
        return logData;
      }).collect(Collectors.toList());

      list.sort((o1, o2) -> desc ? o2.getTotal().compareTo(o1.getTotal()) : o1.getTotal().compareTo(o2.getTotal()));

      if (list.size() >= top) {
        list = list.subList(0, top);
      }
    }

    return list;
  }

  @Override
  public List<OperLogMonthData> totalMonthList(Date start, Date end, Integer top) {

    List<OperLogMonthData> list = new ArrayList<>();
    List<Object[]> dataList = baseDao.totalMonthList(start, end);

    if (CollectionUtils.isNotEmpty(dataList)) {
      List<OperLogData> logList = new ArrayList<>();

      for (Object[] arr : dataList) {
        String beanName = arr[0].toString();
        String method = arr[1].toString();
        String operTime = arr[2].toString();
        Long count = Long.valueOf(arr[3].toString());
        String title = arr[4].toString();

        OperLogData data = new OperLogData(title, beanName, method, operTime, count);
        logList.add(data);

      }

      Map<String, List<OperLogData>> data = logList.stream().collect(Collectors.groupingBy(OperLogData::getOperTime));

      for (Map.Entry<String, List<OperLogData>> entry : data.entrySet()) {
        List<OperLogData> operLogDataList = entry.getValue();
        operLogDataList.sort((o1, o2) -> o2.getTotal().compareTo(o1.getTotal()));
        if (operLogDataList.size() > top) {
          operLogDataList = operLogDataList.subList(0, top);
        }
        OperLogMonthData monthData = new OperLogMonthData(entry.getKey(), operLogDataList);
        list.add(monthData);
      }

      list.sort((o1, o2) -> o1.getTime().compareTo(o2.getTime()));
    }

    List<OperLogMonthData> monthList = new ArrayList<>();


    int dff = Math.abs(DateTools.getMonthDifference(end, start));

    Map<String, List<OperLogData>> dataMap = new HashMap<>();
    for (OperLogMonthData monthData : list) {
      dataMap.put(monthData.getTime(), monthData.getDataList());
    }


    for (int i = 0; i < dff; i++) {
      Date st1 = DateTools.addMonths(start, i);
      String month = DateFormatUtils.format(st1, "yyyy-MM");
      List<OperLogData> logDataList = (List<OperLogData>) MapUtils.getObject(dataMap, month, new ArrayList<>());
      OperLogMonthData monthData = new OperLogMonthData(month, logDataList);
      monthList.add(monthData);
    }

    return monthList;
  }

  @Override
  public List<OperLogUserData> totalUserList(Date start, Date end, TimeType type) {
    List<OperLogUserData> list = new ArrayList<>();
    end = null == end ? DateUtils.addDays(new Date(), 1) : end;
    start = initDay(start, end, type);


    List<Object[]> dataList = baseDao.totalUserList(start, end);

    List<OperLogUserData> totalList = dataList.stream().map(arr -> {
      String time = arr[0].toString();
      Long users = Long.valueOf(arr[1].toString());
      Long count = Long.valueOf(arr[2].toString());
      OperLogUserData data = new OperLogUserData(time, users, count);
      return data;

    }).collect(Collectors.toList());
    switch (type) {
      case day:
        list = getDayDataList(totalList, start, end);
        break;
      case week:
        list = getWeekDataList(totalList, start, end);
        break;
      case month:
        list = getMonthDataList(totalList, start, end);
        break;
      case year:
        list = getYearDataList(totalList, start, end);
        break;
    }


    return list;
  }

  private Date initDay(Date start, Date end, TimeType type) {
    if (null == end)
      end = DateUtils.addDays(new Date(), 1);
    if (null == start) {
      switch (type) {
        case day:
          start = DateTools.addDays(end, -1);
          break;
        case week:
          start = DateTools.addWeeks(end, -1);
          break;
        case month:
          start = DateTools.addMonths(end, -1);
          break;
        case year:
          start = DateTools.addDays(end, -1);
          break;
      }
    }
    return start;
  }


  private List<OperLogUserData> getDayDataList(List<OperLogUserData> dataList, Date start, Date end) {

    int dff = Math.abs(DateTools.getDayDifference(end, start));
    List<OperLogUserData> list = new ArrayList<>();

    for (int i = 0; i < dff; i++) {
      Date date = DateTools.addDays(start, i);
      String time = DateFormatUtils.format(date, "yyyy-MM-dd");
      OperLogUserData data = dataList.stream().filter(d -> d.getTime().equals(time)).findFirst().orElse(new OperLogUserData(time, 0L, 0L));
      list.add(data);
    }

    return list;
  }


  private List<OperLogUserData> getYearDataList(List<OperLogUserData> dataList, Date start, Date end) {
    // 计算起始日期和结束日期之间相隔的年数
    Calendar startCal = Calendar.getInstance();
    startCal.setTime(start);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(end);

    int yearsDiff = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);

    List<OperLogUserData> list = new ArrayList<>();

    for (int i = 0; i <= yearsDiff; i++) {
      int currentYear = startCal.get(Calendar.YEAR) + i;

      // 累加这一年的数据
      long totalUsers = 0L;
      long totalCount = 0L;

      // 遍历所有数据，找到匹配年份的数据进行累加
      for (OperLogUserData data : dataList) {
        if (data.getTime().startsWith(String.valueOf(currentYear))) {
          totalUsers += data.getUsers();
          totalCount += data.getCount();
        }
      }

      // 格式化年份为字符串
      String time = String.valueOf(currentYear) + "-01-01";
      OperLogUserData yearData = new OperLogUserData(time, totalUsers, totalCount);
      list.add(yearData);
    }

    return list;
  }

  private List<OperLogUserData> getMonthDataList(List<OperLogUserData> dataList, Date start, Date end) {
    // 计算起始日期和结束日期之间相隔的月数
    Calendar startCal = Calendar.getInstance();
    startCal.setTime(start);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(end);

    int monthsDiff = (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12
            + endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);

    List<OperLogUserData> list = new ArrayList<>();

    for (int i = 0; i <= monthsDiff; i++) {
      Calendar monthCal = (Calendar) startCal.clone();
      monthCal.add(Calendar.MONTH, i);

      // 格式化月份为字符串
      String time = DateFormatUtils.format(monthCal.getTime(), "yyyy-MM");

      // 累加这个月的数据
      long totalUsers = 0L;
      long totalCount = 0L;

      // 遍历所有数据，找到匹配月份的数据进行累加
      for (OperLogUserData data : dataList) {
        if (data.getTime().startsWith(time)) {
          totalUsers += data.getUsers();
          totalCount += data.getCount();
        }
      }

      OperLogUserData monthData = new OperLogUserData(time + "-01", totalUsers, totalCount);
      list.add(monthData);
    }

    return list;
  }

  private List<OperLogUserData> getWeekDataList(List<OperLogUserData> dataList, Date start, Date end) {
    // 计算起始日期和结束日期之间相隔的周数
    Calendar startCal = Calendar.getInstance();
    startCal.setTime(start);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(end);

    // 设置起始日期为周一
    startCal.setFirstDayOfWeek(Calendar.MONDAY);
    startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

    // 设置结束日期为周日
    endCal.setFirstDayOfWeek(Calendar.MONDAY);
    endCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

    // 计算周数差
    long diffInMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
    int weeksDiff = (int) (diffInMillis / (7 * 24 * 60 * 60 * 1000));

    List<OperLogUserData> list = new ArrayList<>();

    for (int i = 0; i <= weeksDiff; i++) {
      Calendar weekStart = (Calendar) startCal.clone();
      weekStart.add(Calendar.WEEK_OF_YEAR, i);

      // 计算这一周的开始和结束日期
      Calendar weekEnd = (Calendar) weekStart.clone();
      weekEnd.add(Calendar.DAY_OF_YEAR, 6); // 周日是这一周的结束

      // 格式化周起始日期为字符串
      String time = DateFormatUtils.format(weekStart.getTime(), "yyyy-MM-dd");

      // 累加这一周内每天的数据
      long totalUsers = 0L;
      long totalCount = 0L;

      // 遍历这一周的每一天
      Calendar day = (Calendar) weekStart.clone();
      while (!day.after(weekEnd)) {
        String dayStr = DateFormatUtils.format(day.getTime(), "yyyy-MM-dd");
        Optional<OperLogUserData> dayData = dataList.stream()
                .filter(d -> d.getTime().equals(dayStr))
                .findFirst();

        if (dayData.isPresent()) {
          totalUsers += dayData.get().getUsers();
          totalCount += dayData.get().getCount();
        }

        day.add(Calendar.DAY_OF_YEAR, 1);
      }

      OperLogUserData data = new OperLogUserData(time, totalUsers, totalCount);
      list.add(data);
    }

    return list;
  }


  @Override
  public Boolean delete(Long id) {
    return baseDao.remove(id);
  }

  private void saveReceive(OperLogVo vo) {
    SysOperLog data = new SysOperLog();

    BeanUtils.copyProperties(vo, data, CommonUtils.getNullPropertyNames(vo));
    String cu = vo.getOperName();
    LocalDateTime now = LocalDateTime.now();
    data.setFcu(cu);
    data.setLcu(cu);
    data.setFcd(now);
    data.setLcd(now);
    data.setStatus(UseStatus.NO.getCode());

    setLogIp(data);

    super.baseDao.save(data);
  }

  private void setLogIp(SysOperLog data) {

    String ip = data.getOperIp();

    if (null != ip) {
      SysIPData ipData = ipAddressDao.getByIpaddr(ip);
      if (null != ipData) {
        String location = ipData.getLocation();
        data.setOperLocation(location);
      }
    }

  }
}
