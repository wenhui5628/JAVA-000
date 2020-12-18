package com.wwh.dtx.tccdemo.bank1.spring;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wuwenhui.
 */
@FeignClient(value="tcc-demo-bank2",fallback=Bank2ClientFallback.class)
public interface Bank2Client {
    //远程调用B账户的外汇买卖微服务
    @GetMapping("/bank2/exchangeTrading")
    @Hmily
    public Boolean exchangeTrading(@RequestParam("buyAccountNo") String buyAccountNo,@RequestParam("buyCcy") String buyCcy,
                                    @RequestParam("sellAccountNo") String sellAccountNo,@RequestParam("sellCcy") String sellCcy,
                                    @RequestParam("buyAmount") Double buyAmount,@RequestParam("sellAmount") Double sellAmount);
}
