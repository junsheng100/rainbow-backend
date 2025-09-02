package com.rainbow.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "job.operation")
public class JobConfig {

  private String url;

  private Boolean enabled;

  private Integer retentionDays;


}
