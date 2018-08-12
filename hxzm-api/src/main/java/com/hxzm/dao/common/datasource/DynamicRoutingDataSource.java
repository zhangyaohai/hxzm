package com.hxzm.dao.common.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Set dynamic DataSource to Application Context
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        if(DatabaseContextHolder.getDataSource() == null){
            DatabaseContextHolder.setDataSource(DatabaseType.CmsDS);
        }
        logger.info("========================================================Current DataSource is [{}]", DatabaseContextHolder.getDataSource().getValue());
        return DatabaseContextHolder.getDataSource().getValue();
    }
}
