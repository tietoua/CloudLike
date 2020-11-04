package com.sdl.cloudlike.provider.controller;

import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.common.vo.R;
import com.sdl.cloudlike.provider.service.intf.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

/**
 * @title: LikeController
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/31 10:29
 */
@RestController
@RequestMapping("/provider/like/")
@RefreshScope
public class LikeController {
    @Autowired
    private LikeService service;

    @Value("${like.score}") //来自同一配置中心
    private int score;//积分奖励
    //点赞
    @PostMapping("dz.do")
    public R dz(@RequestBody LikeAddDto dto){
        System.out.println("点赞给积分："+score);
        return service.likev1(dto);
    }
    //查询
    @GetMapping("count.do")
    public R all(){
        return service.queryCount();
    }

}


















