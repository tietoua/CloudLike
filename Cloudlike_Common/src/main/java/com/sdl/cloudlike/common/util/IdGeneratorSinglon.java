package com.sdl.cloudlike.common.util;

/**
 * @program: CloudLike
 * @description: 单例
 * @author: Feri(邢朋辉)
 * @create: 2020-11-03 10:31
 */
public class IdGeneratorSinglon {
    private IdGeneratorSinglon(){
        generator=new IdGenerator();
    }
    private static IdGeneratorSinglon singlon=new IdGeneratorSinglon();
    public IdGenerator generator;
    public static IdGeneratorSinglon getInstance(){
        return singlon;
    }
}
