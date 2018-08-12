package com.hxzm.dao.common.datasource;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * PayDS 支付数据库changchong_pay
 * CmsDS 数据库 cms
 * SiteDS changchong_site
 *
 *
 * @author zhangyaohai
 * @create 2018-07-26 15:23
 **/
public enum DatabaseType {

    PayDS("pay"),CmsDS("cms"),SiteDS("site"),CctestDS("cctest");

    private @Setter @Getter String value;

    DatabaseType(String value){
        this.value = value;
    }
}
