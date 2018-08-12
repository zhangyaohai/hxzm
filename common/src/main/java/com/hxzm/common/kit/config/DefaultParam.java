package com.hxzm.common.kit.config;

public enum DefaultParam {

    REQUEST_DEFAULT_VALUE("-1"), ZERO("0");

    private String value;

    DefaultParam(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }

}
