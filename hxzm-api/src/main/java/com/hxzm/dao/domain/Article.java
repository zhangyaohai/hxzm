package com.hxzm.dao.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private Integer id;

    private String title;

    private String content;

    private Integer userId;

    private Integer topicId;

    private String tag;

    private String keywords;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer updateUserId;


}