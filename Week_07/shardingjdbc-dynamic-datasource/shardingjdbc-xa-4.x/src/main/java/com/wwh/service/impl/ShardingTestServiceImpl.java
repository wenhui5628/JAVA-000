package com.wwh.service.impl;

import com.wwh.dao.db01.UserMapper;
import com.wwh.dao.db02.AccountMapper;
import com.wwh.service.ShardingTestService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @Author wuwenhui
 * @Date 2020/7/12 下午2:42
 **/
@Service
public class ShardingTestServiceImpl implements ShardingTestService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Override
    @ShardingTransactionType(TransactionType.XA)
    @Transactional
    public void testTransaction() {
        int userResult = userMapper.updateUserName("jiyuge", 1);
        int accountResult = accountMapper.accountAddBalance(BigDecimal.ONE, 1);
        System.out.println("用户表返回数量：" + userResult);
        System.out.println("账户表返回数量："+ accountResult);
//        throw new RuntimeException("我是一个异常");
    }
}
