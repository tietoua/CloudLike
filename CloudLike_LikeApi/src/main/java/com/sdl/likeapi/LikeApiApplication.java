package com.sdl.likeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * @title: LikeApiApplication
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/31 10:41
 */
@SpringBootApplication
@EnableDiscoveryClient
@RibbonClients
public class LikeApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeApiApplication.class,args);
    }
}
