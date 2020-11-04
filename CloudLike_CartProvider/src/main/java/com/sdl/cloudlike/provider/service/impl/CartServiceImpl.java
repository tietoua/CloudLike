package com.sdl.cloudlike.provider.service.impl;

import com.sdl.cloudlike.common.config.RabbitMQTypeConfig;
import com.sdl.cloudlike.common.config.RedisKeyConfig;
import com.sdl.cloudlike.common.dto.*;
import com.sdl.cloudlike.common.third.RedissonUtil;
import com.sdl.cloudlike.common.util.IdGeneratorSinglon;
import com.sdl.cloudlike.common.vo.R;
import com.sdl.cloudlike.entity.Cart;
import com.sdl.cloudlike.provider.config.RabbitMQConfig;
import com.sdl.cloudlike.provider.dao.CartDao;
import com.sdl.cloudlike.provider.service.intf.CartService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.smartcardio.Card;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @title: CartServiceImpl
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/4 10:53
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao dao;
    @Autowired
    private RabbitTemplate template;
    @Override
    public R addJoinCartV1(CartAddDto dto) {
        //校验
        if (dto!=null){
            if (dto.getSkuid()>0 && dto.getCount()>0 && dto.getUid()>0 &&dto.getJprice()>0){
                Cart cart = dao.querByUid(dto);
                if (cart==null){
                    //第一次
                    if (dao.insert(dto)>0){
                        return R.ok();
                    }
                }else{
                    //之前添加
                    if (dao.update(dto)>0){
                        return R.ok();
                    }
                }
            }
        }

        return R.fail();
    }
    //加
    @Override
    public R plusCountV1(CartItemDto dto) {
        if (dto!=null){
            if (dto.getCount()>0 && dto.getId()>0){
                if (dao.updateByid(dto)>0){
                    return R.ok();
                }
            }
        }
        return R.fail();
    }
    //减
    @Override
    public R minusCountV1(CartItemDto dto) {
        if (dto!=null){
            if (dto.getCount()>0 && dto.getId()>0){
                dto.setCount(-dto.getCount());
                if (dao.updateByid(dto)>0){
                    return R.ok();
                }
            }
        }

        return R.fail();
    }

    @Override
    public R delCart(int id) {
        if (id>0){
            if (dao.delById(id)>0){
                return R.ok();
            }
        }
        return R.fail();
    }
    @Override
    public R all(int uid) {
        String k=RedisKeyConfig.CART_UID+uid;
        if(RedissonUtil.checkKey(k)){
            return R.ok(RedissonUtil.getHash(k));
        }else {
            List<Cart> list=dao.queryByUidAll(uid);
            Map<String,Object> map=new LinkedHashMap<>();
            //将数据库的添加到Map
            for(Cart c:list){
                map.put(c.getSkuid()+"",c);
            }
            RedissonUtil.setHashAll(k,map);
            //设置有效期
            RedissonUtil.setTime(k,RedisKeyConfig.CART_TIME, TimeUnit.HOURS);
            return R.ok(list);
        }
    }

    @Override
    public R addJoinCartV2(CartAddDto dto) {
        String k=RedisKeyConfig.CART_UID+dto.getUid();
        MqMsgDto msgDto=new MqMsgDto();
        if(RedissonUtil.checkKey(k)){
            //校验当前的商品之前有没有加入过
            if(RedissonUtil.checkField(k,dto.getSkuid()+"")){
                Cart cart=(Cart)RedissonUtil.getHash(k,dto.getSkuid()+"");
                cart.setScount(cart.getScount()+dto.getCount());
                //之前加入过 更改数量
                RedissonUtil.setHash(k,dto.getSkuid()+"",cart);
                msgDto.setType(RabbitMQTypeConfig.MQ_CART_UPDATE);
            }else {
                //商品第一次加入
                Cart cart=new Cart(dto.getUid(),dto.getSkuid(),dto.getCount(),dto.getJprice());
                RedissonUtil.setHash(k,dto.getSkuid()+"",cart);
                msgDto.setType(RabbitMQTypeConfig.MQ_CART_ADD);
            }
        }else{ //Redis不存在 原因（1.失效了 2.第一次操作购物车）
            //查询数据库
            List<Cart> list=dao.queryByUidAll(dto.getUid());
            if(list==null){ //购物车空了
                //购物车没有任何商品
                Cart cart=new Cart(dto.getUid(),dto.getSkuid(),dto.getCount(),dto.getJprice());
                RedissonUtil.setHash(k,dto.getSkuid()+"",cart);
                msgDto.setType(RabbitMQTypeConfig.MQ_CART_ADD);
            }else { //购物车有内容  同步
                Map<String,Object> map=new LinkedHashMap<>();
                boolean r=true;
                //将数据库的添加到Map
                for(Cart c:list){
                    //验证是否为当前要加入购物车的商品
                    if(c.getSkuid()==dto.getSkuid()){
                        r=false;
                        c.setScount(c.getScount()+dto.getCount());
                        msgDto.setType(RabbitMQTypeConfig.MQ_CART_UPDATE);
                    }
                    map.put(c.getSkuid()+"",c);
                }
                //当前添加的商品不在购物车 第一次
                if(r){
                    map.put(dto.getSkuid()+"",new Cart(dto.getUid(),dto.getSkuid(),dto.getCount(),dto.getJprice()));
                    msgDto.setType(RabbitMQTypeConfig.MQ_CART_ADD);
                }
                //更新
                RedissonUtil.setHashAll(k,map);
            }
            //设置有效期
            RedissonUtil.setTime(k,RedisKeyConfig.CART_TIME, TimeUnit.HOURS);
        }
        msgDto.setId(IdGeneratorSinglon.getInstance().generator.nextId());
        msgDto.setData(dto);
        template.convertAndSend("", RabbitMQConfig.qname_cart,msgDto);
        return R.ok();
    }

    @Override
    public R plusCountV2(CartItemRedisDto dto) {
        String k=RedisKeyConfig.CART_UID+dto.getUid();
        if(RedissonUtil.checkKey(k)){
            if(RedissonUtil.checkField(k,dto.getSkuid()+"")) {
                //加
                Cart cart = (Cart) RedissonUtil.getHash(k, dto.getSkuid() + "");
                cart.setScount(cart.getScount() + dto.getCount());
                RedissonUtil.setHash(k, dto.getSkuid() + "", cart);
                MqMsgDto msgDto=new MqMsgDto();
                msgDto.setType(RabbitMQTypeConfig.MQ_CART_UPDATE);
                msgDto.setId(IdGeneratorSinglon.getInstance().generator.nextId());
                msgDto.setData(dto);
                template.convertAndSend("", RabbitMQConfig.qname_cart,msgDto);
                return R.ok();
            }
        }
        return R.fail();
    }

    @Override
    public R minusCountV2(CartItemRedisDto dto) {
        String k=RedisKeyConfig.CART_UID+dto.getUid();
        if(RedissonUtil.checkKey(k)){
            if(RedissonUtil.checkField(k,dto.getSkuid()+"")) {
                //加
                Cart cart = (Cart) RedissonUtil.getHash(k, dto.getSkuid() + "");
                cart.setScount(cart.getScount() - dto.getCount());
                RedissonUtil.setHash(k, dto.getSkuid() + "", cart);
                MqMsgDto msgDto=new MqMsgDto();
                msgDto.setType(RabbitMQTypeConfig.MQ_CART_UPDATE);
                msgDto.setId(IdGeneratorSinglon.getInstance().generator.nextId());
                dto.setCount(-dto.getCount());
                msgDto.setData(dto);
                template.convertAndSend("", RabbitMQConfig.qname_cart,msgDto);
                return R.ok();
            }
        }
        return R.fail();
    }

    @Override
    public R delCartV2(CartDelDto delDto) {
        if(delDto!=null){
            if(delDto.getUid()>0 && delDto.getSkuid()>0){
                //删除
                if(RedissonUtil.checkKey(RedisKeyConfig.CART_UID+delDto.getUid())){
                    //删除 用户购物车中某一条数据
                    RedissonUtil.delHash(RedisKeyConfig.CART_UID+delDto.getUid(),delDto.getSkuid()+"");
                }
                if(dao.delByUid(delDto)>0){
                    return R.ok();
                }
            }
        }
        return R.fail();
    }
}


