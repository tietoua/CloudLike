package com.sdl.cloudlike.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: CloudLike
 * @description:
 * @author: Feri(邢朋辉)
 * @create: 2020-11-04 10:25
 */
@Data
public class CartItemRedisDto implements Serializable {
    private int uid;
    private int skuid;
    private int count;//+ 正数  - 负数
}
