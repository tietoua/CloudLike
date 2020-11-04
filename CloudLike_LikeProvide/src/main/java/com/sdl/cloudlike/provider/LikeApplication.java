package com.sdl.cloudlike.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @title: LikeApplication
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/30 17:08
 */
@SpringBootApplication
@MapperScan(basePackages = "com.sdl.cloudlike.provider.dao")
@EnableDiscoveryClient
@EnableScheduling//启用定时是任务
public class LikeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeApplication.class,args);
    }
}
