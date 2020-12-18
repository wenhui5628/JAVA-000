package com.wwh.dtx.tccdemo.bank2.controller;

import com.wwh.dtx.tccdemo.bank2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuwenhui
 * @version 1.0
 **/
@RestController
public class Bank2Controller {
    @Autowired
    AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("amount") Double amount) {
        this.accountInfoService.updateAccountBalance("2", amount);
        return true;
    }

    /***
     * 3、（必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github：
     * 1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；
     * 2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元；
     * 3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务
     */
    @RequestMapping("/exchangeTrading")
    public Boolean exchangeTrading(@RequestParam("buyAccountNo") String buyAccountNo, @RequestParam("buyCcy") String buyCcy, @RequestParam("sellAccountNo") String sellAccountNo, @RequestParam("sellCcy") String sellCcy, @RequestParam("buyAmount") Double buyAmount, @RequestParam("sellAmount") Double sellAmount) {
        this.accountInfoService.exchangeTrading(buyAccountNo, buyCcy, sellAccountNo, sellCcy, buyAmount, sellAmount);
        return true;
    }

}
