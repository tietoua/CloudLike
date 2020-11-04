package com.sdl.cloudlike.provider.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * @title: RabbitMQConfig
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/3 15:04
 */

public class RabbitMQConfig {
    public static String qname_like="like-sync";
    @Bean
    public Queue creatQu(){
        return new Queue(qname_like);
    }
}
