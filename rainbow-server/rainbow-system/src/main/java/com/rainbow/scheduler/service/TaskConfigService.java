package com.rainbow.scheduler.service;

import com.rainbow.base.service.BaseService;
import com.rainbow.scheduler.entity.TaskConfig;

/**
 * 任务配置服务接口
 */
public interface TaskConfigService extends BaseService<TaskConfig,String> {
    

    /**
     * 启动任务
     */
    void startTask(String id);


    void startTask(TaskConfig config);

    /**
     * 停止任务
     */
    void stopTask(String id);
    
    /**
     * 立即执行一次
     */
    void executeOnce(String id);

    /**
     * 验证cron表达式
     */
    boolean validateCronExpression(String cronExpression);

    /***
     * 根据任务名称查询任务
     * @param taskName
     * @return
     */

    TaskConfig findByTaskName(String taskName);

 }