package com.sdl.cloudlike.entity;

import lombok.Data;

import java.util.Date;

/**
 * @title: Like
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/30 17:22
 */
@Data
public class Like {
    private Integer id;
    private Integer cid;
    private Integer uid;
    private Date ctime;
}
