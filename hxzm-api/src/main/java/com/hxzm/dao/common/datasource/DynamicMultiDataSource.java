package com.hxzm.dao.common.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态多数据源
 *
 * @author zhangyaohai
 * @create 2018-07-26 15:54
 **/
public class DynamicMultiDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(DynamicMultiDataSource.class);


    public DynamicMultiDataSource(){
        logger.info("===========init===================================dsKey = " + DatabaseContextHolder.getDataSource());

    }

    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("==============================================dsKey = " + DatabaseContextHolder.getDataSource().getValue());
        return  DatabaseContextHolder.getDataSource().getValue();
    }
}
