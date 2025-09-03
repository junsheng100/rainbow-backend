package com.rainbow;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true)
@EntityScan(basePackages = {"com.rainbow.**.entity"})
@ComponentScan(basePackages = {"com.rainbow"})
@EnableJpaRepositories(basePackages = {"com.rainbow.**.repository"})
@EnableJpaAuditing
public class SystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(SystemApplication.class, args);

    log.info("######## Rainbow-Auth start .....");

  }



}
