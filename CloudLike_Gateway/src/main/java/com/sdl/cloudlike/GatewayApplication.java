package com.sdl.cloudlike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @title: GatewayApplication
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/2 14:20
 */
@SpringBootApplication
@EnableDiscoveryClient//注册和发现服务
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
