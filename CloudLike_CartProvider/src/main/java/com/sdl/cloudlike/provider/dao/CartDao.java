package com.sdl.cloudlike.provider.dao;

import com.sdl.cloudlike.common.dto.CartAddDto;
import com.sdl.cloudlike.common.dto.CartDelDto;
import com.sdl.cloudlike.common.dto.CartItemDto;
import com.sdl.cloudlike.entity.Cart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: CarDao
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/4 10:51
 */
@Repository
public interface CartDao {
    //查询skuid是否存在
    @Select("select * from t_cart where skuid=#{skuid} and uid = #{uid}")
    Cart querByUid(CartAddDto dto);
    @Select("select * from t_cart where uid=#{uid}")
    List<Cart> queryByUidAll(int uid);
    @Insert("insert into t_cart(skuid,uid,jprice,scount,ctime) values(#{skuid},#{uid},#{jprice},#{scount},now())")
    int insert(CartAddDto dto);
    @Update("update t_cart set scount=count=count+#{count} where skuid=#{skuid},uid=#{uid}")
    int update(CartAddDto dto);
    @Update("update t_cart set scount=scount=count+#{count} where uid=#{uid}")
    int updateByid(CartItemDto dto);
    @Delete("delect from t_cart where id=#{id}")
    int delById(int id);
    @Delete("delete from t_cart where uid=#{uid} and skuid=#{skuid}")
    int delByUid(CartDelDto delDto);
}
