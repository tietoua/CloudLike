package com.sdl.cloudlike.provider.controller;

import com.sdl.cloudlike.common.dto.CartAddDto;
import com.sdl.cloudlike.common.dto.CartDelDto;
import com.sdl.cloudlike.common.dto.CartItemDto;
import com.sdl.cloudlike.common.dto.CartItemRedisDto;
import com.sdl.cloudlike.common.vo.R;
import com.sdl.cloudlike.provider.service.intf.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: CloudLike
 * @description:
 * @author: Feri(邢朋辉)
 * @create: 2020-11-04 10:57
 */
@RestController
@RequestMapping("/provider/cart/")
public class CartController {
    @Autowired
    private CartService service;
    @GetMapping("all.do")
    public R all(int uid){
        return service.all(uid);
    }
    @PostMapping("join.do")
    public R join(@RequestBody CartAddDto dto){
        return service.addJoinCartV1(dto);
    }
    @PostMapping("changeminus.do")
    public R changeMinus(@RequestBody CartItemDto dto){
        return service.minusCountV1(dto);
    }
    @PostMapping("changeplus.do")
    public R changePlus(@RequestBody CartItemDto dto){
        return service.plusCountV1(dto);
    }
    @DeleteMapping("del.do")
    public R del(int id){
        return service.delCart(id);
    }

    @PostMapping("joinv2.do")
    public R joinV2(@RequestBody CartAddDto dto){
        return service.addJoinCartV2(dto);
    }
    @PostMapping("changeminusv2.do")
    public R changeMinusV2(@RequestBody CartItemRedisDto dto){
        return service.minusCountV2(dto);
    }
    @PostMapping("changeplusv2.do")
    public R changePlusV2(@RequestBody CartItemRedisDto dto){
        return service.plusCountV2(dto);
    }
    @DeleteMapping("delv2.do")
    public R delV2(@RequestBody CartDelDto delDto){
        return service.delCartV2(delDto);
    }
}
