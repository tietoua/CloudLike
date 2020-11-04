package com.sdl.cloudlike.common.config;

/**
 * @title: RabbitMQTypeConfig
 * @description: 记录rabbirmq消息的类型
 * @Author 宋岱霖
 * @Date: 2020/11/3 15:10
 */

public class RabbitMQTypeConfig {
    //点赞新增
    public static final int MQ_LIKE_ADD=1;
    //点赞删除
    public static final int MQ_LIKE_DEL=2;

    //购物车 新增
    public static final int MQ_CART_ADD=3;
    //购物车修改
    public static final int MQ_CART_UPDATE=4;
    //购物车删除
}
