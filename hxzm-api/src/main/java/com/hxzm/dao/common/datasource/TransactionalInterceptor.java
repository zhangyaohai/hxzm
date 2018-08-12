package com.hxzm.dao.common.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
/**
 * fcjs
 *
 * @author zhangyaohai
 * @create 2018-07-26 15:23
 **/
@Aspect
@Component
@Order(1)
public class TransactionalInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalInterceptor.class);

    @Around("@annotation(transactional)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, Transactional transactional) throws Throwable {
        try {
            logger.info("set transactional to true");
            DatabaseContextHolder.setTransactional(Boolean.TRUE);
            return proceedingJoinPoint.proceed();
        } finally {
            //清除当前线程开关值
            DatabaseContextHolder.clearTransactional();
            logger.info("restore datasource connection");
        }
    }

}
