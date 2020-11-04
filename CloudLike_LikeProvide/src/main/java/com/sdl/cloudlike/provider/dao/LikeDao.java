package com.sdl.cloudlike.provider.dao;

import com.sdl.cloudlike.common.dto.ContentLikeDto;
import com.sdl.cloudlike.common.dto.LikeAddDto;
import com.sdl.cloudlike.entity.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: LikeDao
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/10/30 17:28
 */
@Repository
public interface LikeDao {
    //新增
    @Insert("insert into t_like(uid,cid,ctime) values(#{uid},#{cid},#{ctime})")
    int inset(LikeAddDto dto);

    int[] insertBatch(List<LikeAddDto> likeAddDtos);
    //删除
    @Delete("delect from t_like where uid=#{uid} and cid=#{cid}")
    int delect(LikeAddDto dto);
    //查询 每个内容的点赞记录
    @ResultType(Like.class)
    @Select("select * from t_like where cid=#{cid} order by ctime desc")
    List<Like> queryByCid(int cid);
    //查询是否点赞
    @ResultType(Like.class)
    @Select("select * from t_like where uid=#{uid} and cid=#{cid} limit 1")
    Like querySiangle(LikeAddDto dto);
    //查询点赞的数量
    @Select("select count(*) ct,cid from t_like group by cid")
    List<ContentLikeDto> queryCount();


}
