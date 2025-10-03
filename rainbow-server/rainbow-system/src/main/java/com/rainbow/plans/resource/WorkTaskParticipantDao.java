package com.rainbow.plans.resource;

import com.rainbow.base.resource.BaseDao;
import com.rainbow.plans.entity.WorkPlanTask;
import com.rainbow.plans.entity.WorkTaskParticipant;

import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.resource
 * @Filename：WorkTaskParticipantDao
 * @Date：2025/9/21 17:56
 * @Describe:
 */
public interface WorkTaskParticipantDao extends BaseDao<WorkTaskParticipant, String> {

  List<WorkTaskParticipant> findByTaskId(String taskId);

  void deleteByTaskId(String taskId);

  boolean stortList(WorkPlanTask task, List<WorkTaskParticipant> participants);

  void convertCollection(List<WorkTaskParticipant> participantList);

  List<WorkTaskParticipant> findInTaskId(List<String> taskIdList);

  boolean removeByTaskId(String taskId);
}
