package com.sdl.likeapi.service;

import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.common.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @title: LikeServiceImpl
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/31 10:48
 */
@Service
public class LikeServiceImpl {
    @Autowired
    private RestTemplate template;
    public R dz(LikeAddDto dto){
        return template.postForObject("http://LikeProvider/provider/like/dz.do",dto,R.class);
    }
    public R all(){
        return template.getForObject("http://LikeProvider/provider/like/count.do",R.class);
    }
}
