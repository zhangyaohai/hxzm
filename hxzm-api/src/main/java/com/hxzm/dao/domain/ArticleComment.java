package com.hxzm.dao.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleComment {
    private Integer id;

    private String content;

    private Integer userId;

    private Integer articleId;

    private Date createTime;


}