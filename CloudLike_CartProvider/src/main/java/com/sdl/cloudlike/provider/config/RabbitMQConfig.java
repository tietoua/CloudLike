package com.sdl.cloudlike.provider.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: CloudLike
 * @description:
 * @author: Feri(邢朋辉)
 * @create: 2020-11-03 10:12
 */
@Configuration
public class RabbitMQConfig {
    public static String qname_cart="cart-sync";
    @Bean
    public Queue createQu(){
        return new Queue(qname_cart);
    }
}
