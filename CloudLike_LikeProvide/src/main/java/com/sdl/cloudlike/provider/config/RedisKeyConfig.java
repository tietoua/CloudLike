package com.sdl.cloudlike.provider.config;

import org.omg.CORBA.INTERNAL;

/**
 * @title: RedisKeyConfig
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/2 22:40
 */

public class RedisKeyConfig {
    //记录点赞的内容
    public static String LIKE_CID="like:";//cid
    //点赞热点数据 有效期三个月
    public static int LIKE_TIME=90;

}
