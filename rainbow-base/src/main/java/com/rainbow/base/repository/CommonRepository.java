package com.rainbow.base.repository;

import com.rainbow.base.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<T extends BaseEntity> extends JpaSpecificationExecutor<T> {


}
