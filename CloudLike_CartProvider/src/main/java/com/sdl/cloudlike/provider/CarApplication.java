package com.sdl.cloudlike.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @title: CarApplication
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/4 11:13
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.sdl.cloudlike.provider.dao")
public class CarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class,args);
    }
}
