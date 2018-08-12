package com.hxzm.dao.mapper;

import com.hxzm.dao.domain.Topic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface TopicMapper {
    @Delete({
        "delete from hxzm_topic",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into hxzm_topic (id, topid_name, ",
        "parent_id, sort_num, ",
        "level)",
        "values (#{id,jdbcType=INTEGER}, #{topidName,jdbcType=VARCHAR}, ",
        "#{parentId,jdbcType=VARCHAR}, #{sortNum,jdbcType=INTEGER}, ",
        "#{level,jdbcType=INTEGER})"
    })
    int insert(Topic record);

    @InsertProvider(type=TopicSqlProvider.class, method="insertSelective")
    int insertSelective(Topic record);

    @Select({
        "select",
        "id, topid_name, parent_id, sort_num, level",
        "from hxzm_topic",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="topid_name", property="topidName", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sort_num", property="sortNum", jdbcType=JdbcType.INTEGER),
        @Result(column="level", property="level", jdbcType=JdbcType.INTEGER)
    })
    Topic selectByPrimaryKey(Integer id);

    @UpdateProvider(type=TopicSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Topic record);

    @Update({
        "update hxzm_topic",
        "set topid_name = #{topidName,jdbcType=VARCHAR},",
          "parent_id = #{parentId,jdbcType=VARCHAR},",
          "sort_num = #{sortNum,jdbcType=INTEGER},",
          "level = #{level,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Topic record);
}