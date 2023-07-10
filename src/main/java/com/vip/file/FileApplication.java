package com.vip.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 文件管理服务
 *
 * @author LEON
 */
@MapperScan(basePackages = {"com.vip.file.mapper"})
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@EnableScheduling
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }

}
