package com.wwh.dtx.tccdemo.bank2.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface AccountInfoDao {

    @Update("update account_info set account_balance=account_balance + #{amount} where  account_no=#{accountNo} ")
    int addAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

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


    /**
     * 增加某分支事务try执行记录
     * @param localTradeNo 本地事务编号
     * @return
     */
    @Insert("insert into local_try_log values(#{txNo},now());")
    int addTry(String localTradeNo);

    @Insert("insert into local_confirm_log values(#{txNo},now());")
    int addConfirm(String localTradeNo);

    @Insert("insert into local_cancel_log values(#{txNo},now());")
    int addCancel(String localTradeNo);

    /**
     * 查询分支事务try是否已执行
     * @param localTradeNo 本地事务编号
     * @return
     */
    @Select("select count(1) from local_try_log where tx_no = #{txNo} ")
    int isExistTry(String localTradeNo);
    /**
     * 查询分支事务confirm是否已执行
     * @param localTradeNo 本地事务编号
     * @return
     */
    @Select("select count(1) from local_confirm_log where tx_no = #{txNo} ")
    int isExistConfirm(String localTradeNo);

    /**
     * 查询分支事务cancel是否已执行
     * @param localTradeNo 本地事务编号
     * @return
     */
    @Select("select count(1) from local_cancel_log where tx_no = #{txNo} ")
    int isExistCancel(String localTradeNo);

}
