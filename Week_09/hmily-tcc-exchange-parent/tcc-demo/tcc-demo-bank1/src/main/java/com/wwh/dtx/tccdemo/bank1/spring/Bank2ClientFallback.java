package com.wwh.dtx.tccdemo.bank1.spring;

import org.springframework.stereotype.Component;

/**
 * @author wuwenhui
 * @version 1.0
 **/
@Component
public class Bank2ClientFallback implements Bank2Client {

    @Override
    public Boolean transfer(Double amount) {

        return false;
    }

    @Override
    public Boolean exchangeTrading(String buyAccountNo, String buyCcy, String sellAccountNo, String sellCcy, Double buyAmount, Double sellAmount) {
        return false;
    }
}
