package com.rainbow.config;//package com.rainbow.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StreamUtils;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 检查是否已存在管理员用户
//        Integer count = jdbcTemplate.queryForObject(
//            "SELECT COUNT(*) FROM sys_user_info WHERE user_name = 'admin'",
//            Integer.class
//        );
//
//        if (count == null || count == 0) {
//            // 读取并执行初始化SQL脚本
//            ClassPathResource resource = new ClassPathResource("db/init-data.sql");
//            String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
//            jdbcTemplate.execute(sql);
//            System.out.println("管理员用户初始化完成");
//        }
//    }
//}