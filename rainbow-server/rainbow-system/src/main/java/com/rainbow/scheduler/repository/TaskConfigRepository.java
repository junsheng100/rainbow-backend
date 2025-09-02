package com.rainbow.scheduler.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.scheduler.entity.TaskConfig;

/**
 * 任务配置数据访问接口
 */
public interface TaskConfigRepository extends BaseRepository<TaskConfig, String> {
    

    /**
     * 根据任务名称查询
     */
    TaskConfig findByTaskName(String taskName);

} 