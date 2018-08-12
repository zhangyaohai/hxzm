package com.hxzm.dao.mapper;

import com.hxzm.dao.domain.Topic;
import org.apache.ibatis.jdbc.SQL;

public class TopicSqlProvider {

    public String insertSelective(Topic record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("hxzm_topic");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getTopidName() != null) {
            sql.VALUES("topid_name", "#{topidName,jdbcType=VARCHAR}");
        }
        
        if (record.getParentId() != null) {
            sql.VALUES("parent_id", "#{parentId,jdbcType=VARCHAR}");
        }
        
        if (record.getSortNum() != null) {
            sql.VALUES("sort_num", "#{sortNum,jdbcType=INTEGER}");
        }
        
        if (record.getLevel() != null) {
            sql.VALUES("level", "#{level,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Topic record) {
        SQL sql = new SQL();
        sql.UPDATE("hxzm_topic");
        
        if (record.getTopidName() != null) {
            sql.SET("topid_name = #{topidName,jdbcType=VARCHAR}");
        }
        
        if (record.getParentId() != null) {
            sql.SET("parent_id = #{parentId,jdbcType=VARCHAR}");
        }
        
        if (record.getSortNum() != null) {
            sql.SET("sort_num = #{sortNum,jdbcType=INTEGER}");
        }
        
        if (record.getLevel() != null) {
            sql.SET("level = #{level,jdbcType=INTEGER}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }
}