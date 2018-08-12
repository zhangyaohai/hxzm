package com.hxzm.dao.domain;

public class Topic {
    private Integer id;

    private String topidName;

    private String parentId;

    private Integer sortNum;

    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopidName() {
        return topidName;
    }

    public void setTopidName(String topidName) {
        this.topidName = topidName == null ? null : topidName.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}