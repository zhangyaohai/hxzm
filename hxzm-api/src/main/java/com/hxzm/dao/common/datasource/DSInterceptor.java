package com.hxzm.dao.common.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author zhangyaohai
 * @create 2018-07-27 14:06
 **/
@Aspect
@Component
@Order(0)
public class DSInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DSInterceptor.class);
//payDS,cmsDS,siteDS


    @Around("@annotation(cmsDS)")
    public Object proceedCmsDS1(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            logger.info("===========================set datasource connection to cmsDS");
            DatabaseContextHolder.setDataSource(DatabaseType.CmsDS);
            return proceedingJoinPoint.proceed();
        } finally {
            //清除当前线程开关值
            DatabaseContextHolder.clear();
            logger.info("restore datasource connection");
        }
    }

    @Around("@annotation(payDS)")
    public Object proceedPayDS(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            logger.info("=============================set datasource connection to payDS");
            DatabaseContextHolder.setDataSource(DatabaseType.PayDS);
            return proceedingJoinPoint.proceed();
        } finally {
            //清除当前线程开关值
            DatabaseContextHolder.clear();
            logger.info("restore datasource connection");
        }
    }


    @Around("@annotation(siteDS)")
    public Object proceedSiteDS(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            logger.info("==============================set datasource connection to siteDS");
            DatabaseContextHolder.setDataSource(DatabaseType.SiteDS);
            return proceedingJoinPoint.proceed();
        } finally {
            //清除当前线程开关值
            DatabaseContextHolder.clear();
            logger.info("restore datasource connection");
        }
    }

    @Around("@annotation(cctestDS)")
    public Object proceedcctestDS(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            logger.info("==============================set datasource connection to CctestDS");
            DatabaseContextHolder.setDataSource(DatabaseType.CctestDS);
            return proceedingJoinPoint.proceed();
        } finally {
            //清除当前线程开关值
            DatabaseContextHolder.clear();
            logger.info("restore datasource connection");
        }
    }

}
