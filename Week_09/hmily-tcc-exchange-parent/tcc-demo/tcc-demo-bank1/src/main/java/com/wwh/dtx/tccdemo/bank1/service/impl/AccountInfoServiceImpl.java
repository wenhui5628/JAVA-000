package com.wwh.dtx.tccdemo.bank1.service.impl;

import com.wwh.dtx.tccdemo.bank1.dao.AccountFreezeInfoDao;
import com.wwh.dtx.tccdemo.bank1.dao.AccountInfoDao;
import com.wwh.dtx.tccdemo.bank1.service.AccountInfoService;
import com.wwh.dtx.tccdemo.bank1.spring.Bank2Client;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * @author wuwenhui
 * @version 1.0
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    AccountFreezeInfoDao accountFreezeInfoDao;

    @Autowired
    Bank2Client bank2Client;

    // 账户扣款，就是tcc的try方法

    /**
     * 	try幂等校验
     * 	try悬挂处理
     * 	检查余额是够扣减金额
     * 	扣减金额
     * @param accountNo
     * @param amount
     */
    @Override
    @Transactional
    //只要标记@Hmily就是try方法，在注解中指定confirm、cancel两个方法的名字
    @Hmily(confirmMethod="commit",cancelMethod="rollback")
    public void updateAccountBalance(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 try begin 开始执行...xid:{}",transId);
        //幂等判断 判断local_try_log表中是否有try日志记录，如果有则不再执行
        if(accountInfoDao.isExistTry(transId)>0){
            log.info("bank1 try 已经执行，无需重复执行,xid:{}",transId);
            return ;
        }

        //try悬挂处理，如果cancel、confirm有一个已经执行了，try不再执行
        if(accountInfoDao.isExistConfirm(transId)>0 || accountInfoDao.isExistCancel(transId)>0){
            log.info("bank1 try悬挂处理  cancel或confirm已经执行，不允许执行try,xid:{}",transId);
            return ;
        }

        //扣减金额
        if(accountInfoDao.subtractAccountBalance(accountNo, amount)<=0){
            //扣减失败
            throw new RuntimeException("bank1 try 扣减金额失败,xid:{}"+transId);
        }
        //插入try执行记录,用于幂等判断
        accountInfoDao.addTry(transId);

        //远程调用李四，转账
        if(!bank2Client.transfer(amount)){
            throw new RuntimeException("bank1 远程调用李四微服务失败,xid:{}"+transId);
        }
        if(amount == 2){
            throw new RuntimeException("人为制造异常,xid:{}"+transId);
        }
        log.info("bank1 try end 结束执行...xid:{}",transId);
    }

    /***
     * 针对不同币种的账户扣减金额或者增加金额
     * @param buyAccountNo  买入账户，比如用1美元换7人民币，则这里就是7人民币对应的账号
     * @param buyCcy    买入币种，对于美元换人民币，这里就是人民币
     * @param sellAccount   卖出账户，比如用1美元换7人民币，则此处是1美元对应的账号
     * @param sellCcy   卖出币种，对于美元换人民币，这里就是美元
     * @param buyAmount  买入金额，对于1美元换7人民币，这里就是7
     * @param sellAmount    卖出金额，对于1美元换7人民币，这里就是1
     */
    @Override
    @Transactional
    //只要标记@Hmily就是try方法，在注解中指定confirm、cancel两个方法的名字
    @Hmily(confirmMethod="commitTrade",cancelMethod="rollbackTrade")
    public void exchangeTrading(String buyAccountNo, String buyCcy, String sellAccount, String sellCcy, Double buyAmount, Double sellAmount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        //步骤1：对卖出账户生成一笔冻结记录
        Random r = new Random(1);
        int randomNum = r.nextInt(1);
        accountFreezeInfoDao.insertFreezeInfo(randomNum,sellAccount,sellAmount);
        //步骤2：将卖出账户的账户余额减去要卖出的金额
        int result = accountInfoDao.subtractAccountBalanceByCcy(sellAccount,sellAmount,sellCcy);
        if(result <= 0){
            //扣减金额失败
            throw new RuntimeException("bank1 try 扣减金额失败,xid:{}"+transId);
        }

        //远程调用B的外汇买卖服务
        String buyAccountNoB = "1004";
        String buyCcyB = "USD";
        String sellAccountNoB = "1003";
        String sellCcyB = "CNY";
        Double buyAmountB = new Double(1);
        Double sellAmountB = new Double(7);
        if(!bank2Client.exchangeTrading(buyAccountNoB,buyCcyB,sellAccountNoB,sellCcyB,buyAmountB,sellAmountB)){
            throw new RuntimeException("bank1 远程调用B的外汇买卖服务失败,xid:{}"+transId);
        }
    }

    //confirm方法
    @Transactional
    public void commit(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 confirm begin 开始执行...xid:{},accountNo:{},amount:{}",transId,accountNo,amount);
    }

    //confirm方法
    @Transactional
    public void commitTrade(String buyAccountNo, String buyCcy, String sellAccount, String sellCcy, Double buyAmount, Double sellAmount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 confirm begin 开始执行...xid:{},buyAccountNo:{},buyAmount:{},buyCcy:{}",transId,buyAccountNo,buyAmount,buyCcy);
        //步骤1：将买方账户余额加上相应金额
        accountInfoDao.addAccountBalanceByCcy(buyAccountNo,buyAmount,buyCcy);
        //步骤2：删除冻结记录
        accountFreezeInfoDao.deleteFreezeInfo(sellAccount,sellAmount);
    }



    /** cancel方法
     * 	cancel幂等校验
     * 	cancel空回滚处理
     * 	增加可用余额
     * @param accountNo
     * @param amount
     */
    @Transactional
    public void rollback(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 cancel begin 开始执行...xid:{}",transId);
        //	cancel幂等校验
        if(accountInfoDao.isExistCancel(transId)>0){
            log.info("bank1 cancel 已经执行，无需重复执行,xid:{}",transId);
            return ;
        }
        //cancel空回滚处理，如果try没有执行，cancel不允许执行
        if(accountInfoDao.isExistTry(transId)<=0){
            log.info("bank1 空回滚处理，try没有执行，不允许cancel执行,xid:{}",transId);
            return ;
        }
        //	增加可用余额
        accountInfoDao.addAccountBalance(accountNo,amount);
        //插入一条cancel的执行记录
        accountInfoDao.addCancel(transId);
        log.info("bank1 cancel end 结束执行...xid:{}",transId);

    }

    /***
     *
     * @param buyAccountNo
     * @param buyCcy
     * @param sellAccount
     * @param sellCcy
     * @param buyAmount
     * @param sellAmount
     */
    public void rollbackTrade(String buyAccountNo, String buyCcy, String sellAccount, String sellCcy, Double buyAmount, Double sellAmount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 rollbackTrade begin 开始执行...xid:{}",transId);
    }

}
