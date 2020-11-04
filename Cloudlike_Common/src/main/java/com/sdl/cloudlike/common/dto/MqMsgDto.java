package com.sdl.cloudlike.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: MyMsgDto
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/3 15:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqMsgDto {
    private long id;//唯一id
    private int type;//消息类型
    private Object data;//消息内容
}
