package com.sdl.cloudlike.provider.service.impl;

import com.sdl.cloudlike.common.config.RabbitMQTypeConfig;
import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.common.dto.MqMsgDto;
import com.sdl.cloudlike.common.util.IdGeneratorSinglon;
import com.sdl.cloudlike.common.vo.R;
import com.sdl.cloudlike.entity.Like;
import com.sdl.cloudlike.provider.config.RabbitMQConfig;
import com.sdl.cloudlike.provider.config.RedisKeyConfig;
import com.sdl.cloudlike.provider.dao.LikeDao;
import com.sdl.cloudlike.provider.service.intf.LikeService;
import com.sdl.cloudlike.provider.third.RedissonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @title: LikeServiceImpl
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/31 10:17
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeDao dao;
    @Autowired
    private RabbitTemplate template;

    @Override
    public R likev1(LikeAddDto dto) {
        //发起点赞--->
        if(dto!=null && dto.getCid()>0 && dto.getUid()>0) {
            //1.校验是否点赞
            Like like = dao.querySiangle(dto);
            if (like == null) {
                //需要点赞
                //新增
                if (dao.inset(dto) > 0) {
                    return R.ok();
                } else {
                    return R.fail("亲，点赞失败");
                }
            } else {
                //取消点赞
                //删除
                if (dao.delect(dto) > 0) {
                    return R.ok();
                } else {
                    return R.fail("亲，取消点赞失败，稍后再来");
                }
            }
        }else{
            return R.fail("亲，参数非法");
        }
    }

    //redis =》mysql
    public R likev2odl(LikeAddDto dto) {
        if(dto!=null && dto.getCid()>0 && dto.getUid()>0) {
            String key= RedisKeyConfig.LIKE_CID+dto.getCid();
            //校验是否点赞
            if (RedissonUtil.checkKey(key)){
                //之前是否点赞
                //自己是否点赞过
                if(RedissonUtil.checkZset(key,dto.getUid())){
                    //点过赞
                    //取消点赞
                    if (RedissonUtil.delZSet(key,dto.getUid()+"")){
                        dao.delect(dto);
                        //成功
                        return R.ok();
                    }else {
                        return R.fail("亲，网络故障");
                    }
                }else {
                    RedissonUtil.setZset(key,System.currentTimeMillis(),dto.getUid()+"");
                    dao.inset(dto);
                    return R.ok();
                }
            }else {
                //文章 首次点赞
                RedissonUtil.setZset(key,System.currentTimeMillis(),dto.getUid()+"");
                //mysql
                dao.inset(dto);
                return R.ok();
            }
        }
        return R.fail("请输入合法的参数");
    }
    //更改啊redis注解操作mysql
    @Override
    public R likev2(LikeAddDto dto) {
        if(dto!=null && dto.getCid()>0 && dto.getUid()>0) {
            String key= RedisKeyConfig.LIKE_CID+dto.getCid();
            //校验是否点赞
            if (RedissonUtil.checkKey(key)){
                //之前是否点赞
                //自己是否点赞过
                if(RedissonUtil.checkZset(key,dto.getUid())){
                    //点过赞
                    //取消点赞
                    if (RedissonUtil.delZSet(key,dto.getUid()+"")){

                        //成功
                        return R.ok();
                    }else {
                        return R.fail("亲，网络故障");
                    }
                }else {
                    RedissonUtil.setZset(key,System.currentTimeMillis(),dto.getUid()+"");

                    return R.ok();
                }
            }else {
                //文章 首次点赞
                RedissonUtil.setZset(key,System.currentTimeMillis(),dto.getUid()+"");
                //mysql
                dao.inset(dto);
                return R.ok();
            }
        }
        return R.fail("请输入合法的参数");
    }

    @Override
    public R likev3(LikeAddDto dto) {
        if(dto!=null && dto.getCid()>0 && dto.getUid()>0) {
            String key= RedisKeyConfig.LIKE_CID+dto.getCid();
            //校验是否点赞
            if (RedissonUtil.checkKey(key)){
                //之前是否点赞
                //自己是否点赞过
                if(RedissonUtil.checkZset(key,dto.getUid())){
                    //点过赞
                    //取消点赞
                    if (RedissonUtil.delZSet(key,dto.getUid()+"")){
                        //发送MQ消息
                        //成功
                        template.convertAndSend("", RabbitMQConfig.qname_like,new MqMsgDto(IdGeneratorSinglon.getInstance().generator.nextId(), RabbitMQTypeConfig.MQ_LIKE_DEL,dto));
                        return R.ok();
                    }else {
                        return R.fail("亲，网络故障");
                    }
                }else {
                    RedissonUtil.setZset(key,System.currentTimeMillis(),dto.getUid()+"");
                    //发送mq消息
                    template.convertAndSend("",RabbitMQConfig.qname_like,new MqMsgDto(IdGeneratorSinglon.getInstance().generator.nextId(),RabbitMQTypeConfig.MQ_LIKE_ADD,dto));
                    return R.ok();
                }
            }else {
                //mysql
                List<Like> likeList = dao.queryByCid(dto.getCid());
                if (likeList==null){
                //文章 首次点赞
                RedissonUtil.setZset(key,System.currentTimeMillis(),dto.getUid()+"");
                RedissonUtil.setTime(key,RedisKeyConfig.LIKE_TIME, TimeUnit.DAYS);
                //发送MQ消息
                dao.inset(dto);
                return R.ok();
                }else {
                    boolean r=false;
                    //点过赞 但是redis过期
                    Map<Object,Double> map=new LinkedHashMap<>();
                    for (Like l:likeList){
                        if (l.getUid()==dto.getUid()){
                            r=true;
                        }
                        map.put(l.getUid().toString(),Double.parseDouble(l.getCtime().getTime()+""));
                    }
                    RedissonUtil.setZset(key,map);
                    if (r){
                        //之前点过赞 需要取消
                        //取消点赞
                        if(RedissonUtil.delZSet(key,dto.getUid()+"")){
                            //成功
                            //发送MQ消息
                            template.convertAndSend("", RabbitMQConfig.qname_like,new MqMsgDto(IdGeneratorSinglon.getInstance().generator.nextId(), RabbitMQTypeConfig.MQ_LIKE_DEL,dto));
                            return R.ok();
                        }else{
                            return R.fail("亲,网络故障");
                        }
                    }else{
                        //文章 首次点赞
                        RedissonUtil.setZset(key, System.currentTimeMillis(), dto.getUid() + "");
                        RedissonUtil.setTime(key,RedisKeyConfig.LIKE_TIME, TimeUnit.DAYS);
                        //发送MQ消息
                        template.convertAndSend("", RabbitMQConfig.qname_like, new MqMsgDto(IdGeneratorSinglon.getInstance().generator.nextId(), RabbitMQTypeConfig.MQ_LIKE_ADD, dto));
                        return R.ok();
                    }
                }
            }
        }
        return R.fail("请输入合法的参数");
    }

    @Override
    public R queryCount() {
        return R.ok(dao.queryCount());
    }

    @Override
    public R quetyByCid(int cid) {
        String k=RedisKeyConfig.LIKE_CID+cid;
        if (RedissonUtil.checkKey(k)){
            return R.ok(RedissonUtil.getZset(k));
        }else{
            List<Like> list = dao.queryByCid(cid);
            Map<Object,Double> map=new LinkedHashMap<>();
            for (Like l:list){
                map.put(l.getUid().toString(),Double.parseDouble(l.getCtime().getTime()+""));
            }
            RedissonUtil.setZset(k,map);
            RedissonUtil.setTime(k,RedisKeyConfig.LIKE_TIME, TimeUnit.DAYS);
            return R.ok(map.keySet());
        }
    }
}
