package com.sdl.cloudlike.common.dto;

import lombok.Data;

/**
 * @title: CartAddDto
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/4 20:22
 */
@Data
public class CartAddDto {
    private int skuid;
    private int uid;
    private int jprice;
    private int count;
}
