package com.fwtai.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(order = 2)
@MapperScan(basePackages = {"com.fwtai.mapper"})//xml扫描
public class DataSourceConfig implements TransactionManagementConfigurer, ApplicationContextAware {

    private static ApplicationContext context;

    /**写库数据源*/
    @javax.annotation.Resource
    private DataSource dataSource;

    /**读数据源数量*/
    @Value("${spring.db.slavesize}")
    private Integer slavesize;

    /**数据源路由代理*/
    @Bean
    public AbstractRoutingDataSource routingDataSouceProxy() {
        final RoutingDataSource proxy = new RoutingDataSource(slavesize);
        final Map<Object, Object> targetDataSources = new HashMap<>(slavesize + 1);
        targetDataSources.put(DataSourceType.WRITE.getType(),dataSource);
        for (int i = 0; i < slavesize; i++) {
            final DataSource d = context.getBean("slaveDataSource" + i,DataSource.class);
            targetDataSources.put(i,d);
        }
        proxy.setDefaultTargetDataSource(dataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(routingDataSouceProxy());
        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage("com.fwtai.bean;com.fwtai.entity");//扫描实体别名
        Resource configResource = new ClassPathResource("/mybatis-config.xml");
        bean.setConfigLocation(configResource);
        final ResourcePatternResolver mapperResource = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(mapperResource.getResources("classpath*:mapper/**/**.xml"));
        return bean.getObject();
    }

    //设置事务,事务需要知道当前使用的是哪个数据源才能进行事务处理
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(routingDataSouceProxy());
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }

    @Bean("sqlSessionHandle")//支持 SqlSessionTemplate
    public SqlSessionTemplate getSqlSessionTemplate(final SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}