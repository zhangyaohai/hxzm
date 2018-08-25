package com.hxzm.dao.domain;


import lombok.Data;

@Data
public class Topic {
    private Integer id;

    private String topidName;

    private String parentId;

    private Integer sortNum;

    private Integer level;


}