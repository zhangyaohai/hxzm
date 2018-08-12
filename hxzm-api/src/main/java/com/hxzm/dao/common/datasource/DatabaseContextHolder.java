package com.hxzm.dao.common.datasource;

/**
 * fcjs
 *
 * @author zhangyaohai
 * @create 2018-07-26 15:23
 **/
class DatabaseContextHolder {

    private static final ThreadLocal<DatabaseType> dataSource = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> transactional = new ThreadLocal<>();

    static void setDataSource(DatabaseType dsType) {
        dataSource.set(dsType);
    }

    static DatabaseType getDataSource() {
        return dataSource.get();
    }

    static void clear() {
        if (dataSource.get() != null) {
            dataSource.remove();
        }
    }

    static void setTransactional(Boolean flag) {
        transactional.set(flag);
    }

    static Boolean isTransactional() {
        return transactional.get();
    }

    static void clearTransactional() {
        transactional.remove();
    }

}
