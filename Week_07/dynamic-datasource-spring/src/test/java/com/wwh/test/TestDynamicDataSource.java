package com.wwh.test;

import com.wwh.annotation.DynamicRoutingDataSource;
import com.wwh.bean.Cost;
import com.wwh.dataSource.MultiDataSource;
import com.wwh.mapper.DataSourceMapper;
import com.wwh.service.CostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 动态切换数据源方法一
 *
 * @author wuwenhui
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml",
        "classpath:spring-mybatis.xml"})
public class TestDynamicDataSource {

    @Autowired
    CostService costService;
    @Autowired
    DataSourceMapper dataSourceMapper;

    /**
     * 使用工具类切换数据源
     */
    @Test
    public void test() {
        Cost cost = new Cost(111);
        costService.insert(cost);
        MultiDataSource.setDataSourceKey("dataSource2");
        cost.setMoney(222);
        costService.insert(cost);
        MultiDataSource.toDefault();
        cost.setMoney(333);
        costService.insert(cost);
    }

    /**
     * 注解切换数据源
     */
    @Test
    @DynamicRoutingDataSource("dataSource2")
    public void test2() {
        costService.insert2(new Cost(444));
    }

    /**
     * 注解切换数据源
     */
    @Test
    @DynamicRoutingDataSource("dataSource3")
    public void test3() {
        costService.insert3(new Cost(555));
    }
}
