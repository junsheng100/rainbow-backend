package com.rainbow.scheduler.listener;

import com.rainbow.base.enums.UseStatus;
import com.rainbow.base.model.vo.BaseVo;
import com.rainbow.scheduler.entity.TaskConfig;
import com.rainbow.scheduler.service.TaskConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.scheduler.listener
 * @Filename：ScheduleEventListener
 * @Describe:
 */
@Component
public class ScheduleEventListener {

  @Autowired
  private TaskConfigService service;


  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationEvent(ApplicationReadyEvent event) {

    TaskConfig data = new TaskConfig();
    data.setStatus(UseStatus.NO.getCode());
    data.setDisabled(UseStatus.NO.getData());


    BaseVo<TaskConfig> vo = new BaseVo<>();
    vo.setData(data);

    List<TaskConfig> all = service.list(vo);

    if(CollectionUtils.isNotEmpty(all)){
      for(TaskConfig config:  all){
        service.startTask(config);
      }
    }

  }

}
