package com.sdl.cloudlike.provider.task;

import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.provider.config.RedisKeyConfig;
import com.sdl.cloudlike.provider.dao.LikeDao;
import com.sdl.cloudlike.provider.third.RedissonUtil;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.misc.Cleaner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @title: HelloTask
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/2 23:47
 */
@Component
public class HelloTask {
    @Autowired
    private LikeDao dao;

    //每隔三秒执行一次
    @Scheduled(cron = "0/3 * * * * ?")
    public void t1() {
        System.out.println("aini  tietou " + System.currentTimeMillis() / 1000);
    }

    //实现点赞数据同步到mysql中
    @Scheduled(cron = "0 0 4 * *?")
    public void syncMysqlLike() {
        //当前时间的24小时前
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long ctime = calendar.getTimeInMillis();

        List<LikeAddDto> dtoList = new ArrayList<>();
        //实现点赞同步
        Iterable<String> keys = RedissonUtil.getKeys(RedisKeyConfig.LIKE_CID + "");
        while (keys.iterator().hasNext()) {
            String k = keys.iterator().next();
            Collection<ScoredEntry<Object>> uids = RedissonUtil.getZSet(k, ctime);
            for (ScoredEntry se : uids) {
                dtoList.add(new LikeAddDto(Integer.parseInt(se.getValue().toString()), Integer.parseInt(k.substring(k.lastIndexOf(":") + 1))));
            }
            //Collection<Object> uids = RedissonUtil.getZSet(k);
            //for (Object u:uids){
            //    dtoList.add(new LikeAddDto(Integer.parseInt(u.toString()),Integer.parseInt(k.substring(k.lastIndexOf(":")+1))));
            //
            //}
        }
        //批处理
        dao.insertBatch(dtoList);

    }
}
