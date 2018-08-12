package com.hxzm.dao.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Map;


public interface WelcomeMapper {

    @Select("select * from test1")
    Map find1();

    @Select("select * from test1")
    Map find2();

}
