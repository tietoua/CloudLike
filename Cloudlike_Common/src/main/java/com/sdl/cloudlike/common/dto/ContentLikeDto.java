package com.sdl.cloudlike.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: ContentLikeDto
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/30 17:51
 */
@Data
public class ContentLikeDto {
    private Integer cid;
    private long ct;//点赞的数量
}
