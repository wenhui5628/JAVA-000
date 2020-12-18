package com.wwh.dtx.tccdemo.bank1.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AccountInfoDao {
    /***
     * 根据账号和币种扣减对应币种账户的余额
     * @param sellAccount
     * @param amount
     * @param sellCcy
     * @return
     */
    @Update("update account_info set account_balance=account_balance - #{sellAmount} where account_no=#{sellAccount} and account_ccy=#{sellCcy}")
    int subtractAccountBalanceByCcy(@Param("sellAccount") String sellAccount, @Param("sellAmount") Double amount,@Param("sellCcy") String sellCcy);

    /***
     * 根据账号和币种增加对应币种账户的余额
     * @param accountNo
     * @param amount
     * @param ccy
     * @return
     */
    @Update("update account_info set account_balance=account_balance + #{amount} where account_no=#{accountNo} and account_ccy=#{ccy}")
    int addAccountBalanceByCcy(@Param("accountNo") String accountNo, @Param("amount") Double amount,@Param("ccy") String ccy);
}
