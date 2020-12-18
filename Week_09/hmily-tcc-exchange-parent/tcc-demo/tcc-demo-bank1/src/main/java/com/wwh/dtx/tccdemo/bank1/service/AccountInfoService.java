package com.wwh.dtx.tccdemo.bank1.service;

/**
 * Created by wuwenhui.
 */
public interface AccountInfoService {

    //账户扣款
    public void updateAccountBalance(String accountNo, Double amount);

    /***
     * 外汇买卖交易，针对不同币种的账户扣减金额或者增加金额
     * @param buyAccountNo  买入账户，比如用1美元换7人民币，则这里就是7人民币对应的账号
     * @param buyCcy    买入币种，对于美元换人民币，这里就是人民币
     * @param sellAccount   卖出账户，比如用1美元换7人民币，则此处是1美元对应的账号
     * @param sellCcy   卖出币种，对于美元换人民币，这里就是美元
     * @param buyAmount  买入金额，对于1美元换7人民币，这里就是7
     * @param sellAmount    卖出金额，对于1美元换7人民币，这里就是1
     */
    public void exchangeTrading(String buyAccountNo, String buyCcy, String sellAccount, String sellCcy, Double buyAmount, Double sellAmount);
}
