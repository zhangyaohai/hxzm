package com.hxzm.dao.mapper;

import com.hxzm.dao.domain.ArticleComment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface ArticleCommentMapper {
    @Delete({
        "delete from hxzm_article_comment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into hxzm_article_comment (id, content, ",
        "user_id, article_id, ",
        "create_time)",
        "values (#{id,jdbcType=INTEGER}, #{content,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=INTEGER}, #{articleId,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=DATE})"
    })
    @Options(useGeneratedKeys = true)
    int insert(ArticleComment record);

    @InsertProvider(type=ArticleCommentSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true)
    int insertSelective(ArticleComment record);

    @Select({
        "select",
        "id, content, user_id, article_id, create_time",
        "from hxzm_article_comment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="article_id", property="articleId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE)
    })
    ArticleComment selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ArticleCommentSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ArticleComment record);

    @Update({
        "update hxzm_article_comment",
        "set content = #{content,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "article_id = #{articleId,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=DATE}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ArticleComment record);
}