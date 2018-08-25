package com.hxzm.dao.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;

    private String nickName;

    private String account;

    private String realName;

    private String email;

    private String phone;

    private String image;

    private Integer userType;

    private Integer status;

    private Date createTime;

    private Date registeTime;

    private Date lastLoginTime;


}