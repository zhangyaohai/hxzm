package com.hxzm.dao.mapper;

import com.hxzm.dao.domain.ArticleComment;
import org.apache.ibatis.jdbc.SQL;

public class ArticleCommentSqlProvider {

    public String insertSelective(ArticleComment record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("hxzm_article_comment");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getContent() != null) {
            sql.VALUES("content", "#{content,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }
        
        if (record.getArticleId() != null) {
            sql.VALUES("article_id", "#{articleId,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=DATE}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ArticleComment record) {
        SQL sql = new SQL();
        sql.UPDATE("hxzm_article_comment");
        
        if (record.getContent() != null) {
            sql.SET("content = #{content,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }
        
        if (record.getArticleId() != null) {
            sql.SET("article_id = #{articleId,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=DATE}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}