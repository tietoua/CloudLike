package com.sdl.cloudlike.provider.listener;

import com.sdl.cloudlike.common.config.RabbitMQTypeConfig;
import com.sdl.cloudlike.common.dto.CartAddDto;
import com.sdl.cloudlike.common.dto.MqMsgDto;
import com.sdl.cloudlike.provider.config.RabbitMQConfig;
import com.sdl.cloudlike.provider.dao.CartDao;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: CloudLike
 * @description:
 * @author: Feri(邢朋辉)
 * @create: 2020-11-04 14:44
 */
@Component
@RabbitListener(queues ="cart-sync")
public class CartQueueListener {
    @Autowired
    private CartDao dao;

    @RabbitHandler
    public void handler(MqMsgDto dto){
        if(dto!=null) {
            switch (dto.getType()) {
                case RabbitMQTypeConfig
                        .MQ_CART_ADD:
                    dao.insert((CartAddDto) dto.getData());
                    break;
                case RabbitMQTypeConfig
                        .MQ_CART_UPDATE:
                    dao.update((CartAddDto) dto.getData());
                    break;
            }
        }
    }
}
