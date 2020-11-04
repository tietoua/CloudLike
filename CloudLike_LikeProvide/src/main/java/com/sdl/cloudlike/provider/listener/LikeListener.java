package com.sdl.cloudlike.provider.listener;

import com.sdl.cloudlike.common.config.RabbitMQTypeConfig;
import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.common.dto.MqMsgDto;
import com.sdl.cloudlike.provider.dao.LikeDao;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: LikeListener
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/3 16:09
 */
@Component
@RabbitListener
public class LikeListener {
    @Autowired
    private LikeDao dao;
    @RabbitHandler
    public void handler(MqMsgDto dto){
        //校验消息的类型
        if(dto.getType()== RabbitMQTypeConfig.MQ_LIKE_ADD || dto.getType()==RabbitMQTypeConfig.MQ_LIKE_DEL){
            //获取操作
            switch (dto.getType()){
                case RabbitMQTypeConfig.MQ_LIKE_ADD:
                    //新增点赞
                    dao.inset((LikeAddDto) dto.getData());
                    break;
                case RabbitMQTypeConfig.MQ_LIKE_DEL:
                    //删除点赞
                    dao.delect((LikeAddDto) dto.getData());
                    break;
            }
        }

    }
}













