package com.wwh.dtx.tccdemo.bank1.controller;

import com.wwh.dtx.tccdemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuwenhui
 * @version 1.0
 **/
@RestController
public class Bank1Controller {
    @Autowired
    AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("amount") Double amount) {
        this.accountInfoService.updateAccountBalance("1", amount);
        return true;
    }

    /***
     * 此方法只是为了实现两个外汇买卖的组合操作，因此涉及的金额都在方法内部hard code，后续考虑完善，需求如下：
     * 3、（必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github：
     * 1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；
     * 2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元；
     * 3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务
     */
    @RequestMapping("/exchangeTrading")
    public Boolean exchangeTrading() {
        String buyAccountNo = "1001";
        String buyCcy = "CNY";
        String sellAccountNo = "1002";
        String sellCcy = "USD";
        Double buyAmount = new Double(7);
        Double sellAmount = new Double(1);
        this.accountInfoService.exchangeTrading(buyAccountNo,buyCcy,sellAccountNo,sellCcy,buyAmount,sellAmount);
        return true;
    }
}
