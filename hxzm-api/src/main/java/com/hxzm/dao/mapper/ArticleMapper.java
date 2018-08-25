package com.hxzm.dao.mapper;

import com.hxzm.dao.domain.Article;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface ArticleMapper {
    @Delete({
        "delete from hxzm_article",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into hxzm_article (id, title, ",
        "content, user_id, ",
        "topic_id, tag, keywords, ",
        "status, create_time, ",
        "update_time, update_user_id)",
        "values (#{id,jdbcType=INTEGER}, #{title,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR}, #{userId,jdbcType=INTEGER}, ",
        "#{topicId,jdbcType=INTEGER}, #{tag,jdbcType=VARCHAR}, #{keywords,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=INTEGER}, #{createTime,jdbcType=DATE}, ",
        "#{updateTime,jdbcType=DATE}, #{updateUserId,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true)
    int insert(Article record);

    @InsertProvider(type=ArticleSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true)
    int insertSelective(Article record);

    @Select({
        "select",
        "id, title, content, user_id, topic_id, tag, keywords, status, create_time, update_time, ",
        "update_user_id",
        "from hxzm_article",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="topic_id", property="topicId", jdbcType=JdbcType.INTEGER),
        @Result(column="tag", property="tag", jdbcType=JdbcType.VARCHAR),
        @Result(column="keywords", property="keywords", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
        @Result(column="update_user_id", property="updateUserId", jdbcType=JdbcType.INTEGER)
    })
    Article selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ArticleSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Article record);

    @Update({
        "update hxzm_article",
        "set title = #{title,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "topic_id = #{topicId,jdbcType=INTEGER},",
          "tag = #{tag,jdbcType=VARCHAR},",
          "keywords = #{keywords,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=DATE},",
          "update_time = #{updateTime,jdbcType=DATE},",
          "update_user_id = #{updateUserId,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Article record);
}