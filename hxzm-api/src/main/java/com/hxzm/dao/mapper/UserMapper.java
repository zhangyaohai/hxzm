package com.hxzm.dao.mapper;

import com.hxzm.dao.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface UserMapper {
    @Delete({
        "delete from hxzm_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into hxzm_user (id, nick_name, ",
        "account, real_name, ",
        "email, phone, image, ",
        "user_type, status, ",
        "create_time, registe_time, ",
        "last_login_time)",
        "values (#{id,jdbcType=INTEGER}, #{nickName,jdbcType=VARCHAR}, ",
        "#{account,jdbcType=VARCHAR}, #{realName,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{phone,jdbcType=CHAR}, #{image,jdbcType=VARCHAR}, ",
        "#{userType,jdbcType=INTEGER}, #{status,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=DATE}, #{registeTime,jdbcType=DATE}, ",
        "#{lastLoginTime,jdbcType=DATE})"
    })
    @Options(useGeneratedKeys = true)
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true)
    int insertSelective(User record);

    @Select({
        "select",
        "id, nick_name, account, real_name, email, phone, image, user_type, status, create_time, ",
        "registe_time, last_login_time",
        "from hxzm_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
        @Result(column="account", property="account", jdbcType=JdbcType.VARCHAR),
        @Result(column="real_name", property="realName", jdbcType=JdbcType.VARCHAR),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.CHAR),
        @Result(column="image", property="image", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_type", property="userType", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
        @Result(column="registe_time", property="registeTime", jdbcType=JdbcType.DATE),
        @Result(column="last_login_time", property="lastLoginTime", jdbcType=JdbcType.DATE)
    })
    User selectByPrimaryKey(Integer id);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
        "update hxzm_user",
        "set nick_name = #{nickName,jdbcType=VARCHAR},",
          "account = #{account,jdbcType=VARCHAR},",
          "real_name = #{realName,jdbcType=VARCHAR},",
          "email = #{email,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=CHAR},",
          "image = #{image,jdbcType=VARCHAR},",
          "user_type = #{userType,jdbcType=INTEGER},",
          "status = #{status,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=DATE},",
          "registe_time = #{registeTime,jdbcType=DATE},",
          "last_login_time = #{lastLoginTime,jdbcType=DATE}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(User record);
}