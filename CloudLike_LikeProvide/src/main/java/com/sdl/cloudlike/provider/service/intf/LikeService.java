package com.sdl.cloudlike.provider.service.intf;

import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.common.vo.R;

/**
 * @title: LikeService
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/30 17:54
 */

public interface LikeService {
    //第一版 点赞-mysql
    R likev1(LikeAddDto dto);
    //第二版 点赞- 引入redis
    R likev2(LikeAddDto dto);
    //第三版 点赞- 引入rabbitMQ
    R likev3(LikeAddDto dto);
    //查询文章的点赞数
    R queryCount();

    R quetyByCid(int cid);


}
