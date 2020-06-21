package com.fwtai.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源路由
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/7 23:15
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public class RoutingDataSource extends AbstractRoutingDataSource {

    private AtomicInteger count = new AtomicInteger(0);

    private int readsize;

    public RoutingDataSource(final int slaveTotal) {
        this.readsize = slaveTotal;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final String typeKey = DataSourceContextHolder.getJdbcType();
        if (typeKey == null) {
            logger.error("无法确定数据源");
        }
        if (typeKey.equals(DataSourceType.WRITE.getType())) {
            return DataSourceType.WRITE.getType();
        }
        //读库进行负载均衡
        final int a = count.getAndAdd(1);
        int lookupkey = a % readsize;
        return lookupkey;
    }
}