package com.sdl.likeapi.config;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @title: RibbonConfig
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/31 10:43
 */
@Configuration
public class RibbonConfig {
    @Bean
    @LoadBalanced//启用负载均衡
    public RestTemplate createRT(){
        return new RestTemplate();
    }
    //负载均衡的分发策略
    @Bean
    public IRule creatRule(){
        //最小并发分配
        return new BestAvailableRule();
    }
}
