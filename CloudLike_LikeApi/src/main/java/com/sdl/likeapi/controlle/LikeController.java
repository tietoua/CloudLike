package com.sdl.likeapi.controlle;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.common.vo.R;
import com.sdl.likeapi.service.LikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @title: LikeController
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/31 10:53
 */
@RestController
@RequestMapping("/api/like/")
public class LikeController {
    @Autowired
    private LikeServiceImpl service;
    @PostMapping("dz.do")
    public R dz(@RequestBody LikeAddDto dto){
        return service.dz(dto);
    }
    //查询
    @SentinelResource(value = "count.do",fallback = "error1")
    @GetMapping("count.do")
    public R all(){
        System.out.println(1/0);
        return service.all();
    }
    //降级方法 如果对应的借口出现故障
    public R error1(){
        return R.fail("勤服务暂时不可用");
    }

}
