package com.fwtai.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 动态切换的数据源
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/7 23:14
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Configuration
public class DynamicDataSource{

    private final long maxLifetime = 1800002L;
    private final long connectionTimeout = 600000L;
    private final long idleTimeout = 960000L;// 60000 * 16 = 960000;

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        final HikariDataSource hds = DataSourceBuilder.create().type(HikariDataSource.class).build();
        hds.setConnectionTimeout(connectionTimeout);//连接超时时间：毫秒，如果小于250毫秒，否则被重置为默认值30秒(30*1000)
        hds.setMaxLifetime(maxLifetime);// 连接最大存活时间,默认值是1800000，不等于0且小于30秒，会被重置为默认值30分钟.设置应该比mysql设置的超时时间短
        //如果maxPoolSize小于1，则会被重置。当minIdle<=0被重置为DEFAULT_POOL_SIZE则为10;如果minIdle>0则重置为minIdle的值
        hds.setMaximumPoolSize(128);//最大连接数，小于等于0会被重置为默认值10；大于零小于1会被重置为minimum-idle的值
        //如果idleTimeout+1秒>maxLifetime 且 maxLifetime>0，则会被重置为0（代表永远不会退出）；如果idleTimeout!=0且小于10秒，则会被重置为10秒
        hds.setIdleTimeout(idleTimeout);// 空闲连接超时时间，默认值600000（10分钟），如果大于等于max-lifetime且max-lifetime>0，会被重置为0；不等于0且小于10秒，会被重置为10秒
        hds.setMinimumIdle(64);//最小空闲连接，默认值10，小于0或大于maximum-pool-size，都会重置为maximum-pool-size
        hds.setConnectionTestQuery("SELECT 1 FROM DUAL");
        return hds;
    }

    @Bean(name = "slaveDataSource0")
    @ConfigurationProperties(prefix = "spring.db.slave0")
    public DataSource slaveDataSource0() {
        final HikariDataSource hds = DataSourceBuilder.create().type(HikariDataSource.class).build();
        hds.setConnectionTimeout(connectionTimeout);
        hds.setMaxLifetime(maxLifetime);
        hds.setMaximumPoolSize(129);
        hds.setIdleTimeout(idleTimeout);
        hds.setMinimumIdle(64);
        hds.setReadOnly(true);
        hds.setConnectionTestQuery("SELECT 1 FROM DUAL");
        return hds;
    }

    @Bean(name = "slaveDataSource1")
    @ConfigurationProperties(prefix = "spring.db.slave1")
    public DataSource slaveDataSource1() {
        final HikariDataSource hds = DataSourceBuilder.create().type(HikariDataSource.class).build();
        hds.setConnectionTimeout(connectionTimeout);
        hds.setMaxLifetime(maxLifetime);
        hds.setMaximumPoolSize(130);
        hds.setIdleTimeout(idleTimeout);
        hds.setMinimumIdle(64);
        hds.setReadOnly(true);
        hds.setConnectionTestQuery("SELECT 1 FROM DUAL");
        return hds;
    }
}