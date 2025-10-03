package com.rainbow.plans.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.plans.entity.WorkTaskParticipant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author：QQ:304299340
 * @Package：com.rainbow.plans.repository
 * @Filename：WorkTaskParticipantRepository
 * @Date：2025/9/21 17:55
 * @Describe:
 */
public interface WorkTaskParticipantRepository extends BaseRepository<WorkTaskParticipant,String> {

  @Query("select t from WorkTaskParticipant t where t.taskId = ?1  ")
  List<WorkTaskParticipant> findByTaskId(String taskId);

  @Query("select t from WorkTaskParticipant t where t.taskId in (?1) ")
  List<WorkTaskParticipant> findInTaskId(List<String> taskIdList);
}
