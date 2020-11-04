package com.sdl.cloudlike.provider.service.intf;

import com.sdl.cloudlike.common.dto.CartAddDto;
import com.sdl.cloudlike.common.dto.CartDelDto;
import com.sdl.cloudlike.common.dto.CartItemDto;
import com.sdl.cloudlike.common.dto.CartItemRedisDto;
import com.sdl.cloudlike.common.vo.R;

/**
 * @title: CartService
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/4 10:53
 */

public interface CartService {
    //加入购物车
    R addJoinCartV1(CartAddDto dto);
    //数量增加
    R plusCountV1(CartItemDto dto);
    //数量减少
    R minusCountV1(CartItemDto dto);
    //删除
    R delCart(int id);
    //全部
    R all(int uid);

    //加入购物车
    R addJoinCartV2(CartAddDto dto);
    //数量增加
    R plusCountV2(CartItemRedisDto dto);
    //数量减少
    R minusCountV2(CartItemRedisDto dto);
    //删除
    R delCartV2(CartDelDto dto);
}
