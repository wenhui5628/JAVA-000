package com.wwh.dtx.tccdemo.bank2.service.impl;

import com.wwh.dtx.tccdemo.bank2.dao.AccountFreezeInfoDao;
import com.wwh.dtx.tccdemo.bank2.dao.AccountInfoDao;
import com.wwh.dtx.tccdemo.bank2.service.AccountInfoService;
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

    @Override
    @Hmily(confirmMethod="confirmMethod", cancelMethod="cancelMethod")
    public void updateAccountBalance(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 try begin 开始执行...xid:{}",transId);
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

    /**
     * confirm方法
     * 	confirm幂等校验
     * 	正式增加金额
     * @param accountNo
     * @param amount
     */
    @Transactional
    public void confirmMethod(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 confirm begin 开始执行...xid:{}",transId);
        if(accountInfoDao.isExistConfirm(transId)>0){
            log.info("bank2 confirm 已经执行，无需重复执行...xid:{}",transId);
            return ;
        }
        //增加金额
        accountInfoDao.addAccountBalance(accountNo,amount);
        //增加一条confirm日志，用于幂等
        accountInfoDao.addConfirm(transId);
        log.info("bank2 confirm end 结束执行...xid:{}",transId);
    }



    /**
     * @param accountNo
     * @param amount
     */
    public void cancelMethod(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 cancel begin 开始执行...xid:{}",transId);
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
        log.info("bank2 rollbackTrade begin 开始执行...xid:{}",transId);
    }

}
