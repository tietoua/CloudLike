package com.sdl.cloudlike.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @title: Cart
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/4 11:12
 */
@Data
@NoArgsConstructor
public class Cart {
    private Integer id;
    private Integer uid;
    private Integer skuid;
    private Integer scount;
    private Integer jprice;
    private Date ctime;

    public Cart(Integer uid, Integer skuid, Integer jprice,Integer scount) {
        this.uid = uid;
        this.skuid = skuid;
        this.jprice = jprice;
        this.scount = scount;
    }
}
