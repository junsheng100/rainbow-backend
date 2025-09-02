package com.rainbow.scheduler.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.scheduler.entity.TaskConfigParams;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author：jackson.liu
 * @Package：com.rainbow.scheduler.repository
 * @Filename：TaskConfigParamsRepository
 * @Describe:
 */
public interface TaskConfigParamsRepository extends BaseRepository<TaskConfigParams,String> {

  @Query("select t from TaskConfigParams t where t.configId = ?1 order by t.orderNum ")
  List<TaskConfigParams> findByConfigId(String configId);

}
