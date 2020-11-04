package com.sdl.cloudlike.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: LikeAddDto
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/30 17:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeAddDto {
    private int uid;
    private int cid;
}
