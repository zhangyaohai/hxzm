package com.hxzm.config;

import com.hxzm.dao.common.datasource.DatabaseType;
import com.hxzm.dao.common.datasource.DynamicRoutingDataSource;
import com.hxzm.dao.mapper.WelcomeMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 
 * @author zhangyaohai
 *
 */
@MapperScan(basePackageClasses = WelcomeMapper.class)
@Configuration
public class SpringConfiguration extends WebMvcConfigurerAdapter {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "GET", "POST").
                exposedHeaders("X-TOKEN");
    }

    /**
     * 加载cms库数据源
     * @return DataSource
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.cms")
    public DataSource cmsDS() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 加载支付库数据源 payDS
     * @return DataSource
     */
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.pay")
    public DataSource payDS() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 加载网站库数据源 siteDS
     * @return DataSource
     */
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.site")
    public DataSource siteDS() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.cctest")
    public DataSource cctestDS() {
        return DataSourceBuilder.create().build();
    }


    /**
     * 加载路由数据源
     * @return DynamicMultiDataSource
     */
    @Bean
    public DynamicRoutingDataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.CmsDS.getValue(), cmsDS());
        targetDataSources.put(DatabaseType.PayDS.getValue(), payDS());
        targetDataSources.put(DatabaseType.SiteDS.getValue(), siteDS());
        targetDataSources.put(DatabaseType.CctestDS.getValue(), cctestDS());

        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(cmsDS());
        return dataSource;
    }
    
    /**
     * 加载sqlSessionFactory
     * @return
     * @throws Exception
     */
   @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource());
        return sessionFactory.getObject();
    }
    
    /**
     * 加载transactionManager
     * @return
     * @throws Exception
     */
    @Bean
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

}
