## 作业说明
2、（必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github：
1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；
2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元；
3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。

### 业务场景分析，见下图：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_09/%E5%A4%96%E6%B1%87%E4%B9%B0%E5%8D%96%E4%BA%A4%E6%98%93%E6%A8%A1%E5%9E%8B%E5%88%86%E6%9E%90.png)

### 项目结构说明
工程分为三个模块，分别是  
tcc-demo-bank1  
tcc-demo-bank2  
eureka-test  
其中eureka-test是用于启动eureka，用于服务注册和发现；  
tcc-demo-bank1是用于模拟用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币的操作；  
tcc-demo-bank2是用于模拟用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元的操作；  

### 涉及的数据库表见工程中的hmily-tcc-demo.sql，一共三张表，分别是客户信息表，账户信息表以及账户冻结信息表，如下：
由于人民币账户和美元账户信息属性是一致的，不同的只是币种，故将人民币账户信息和美元账户都存放在账户信息表中，用账户币种字段区分这两种账户信息
账户冻结信息表用于在交易时，扣账过程中先对账户冻结，防止账户余额错乱的情况。
````sql
-- ----------------------------
-- Table structure for account_info 客户信息表
-- ----------------------------
DROP TABLE IF EXISTS `customer_info`;
CREATE TABLE `customer_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_no` varchar(10) DEFAULT NULL COMMENT '客户号',
  `customer_name` varchar(100) DEFAULT NULL COMMENT '客户姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
insert into customer_info(customer_no,customer_name) values('001','A');
-- ----------------------------
-- Table structure for account_info 账户信息表
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_no` varchar(10) DEFAULT NULL COMMENT '客户号',
  `account_no` varchar(100) DEFAULT NULL COMMENT '账号',
  `account_password` varchar(100) DEFAULT NULL COMMENT '账户密码',
  `account_ccy` char(3) COMMENT '账户币种，CNY表示人民币，HKD表示美元',
  `account_balance` double DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
insert into account_info(customer_no,account_no,account_password,account_ccy,account_balance) values('001','1001','111111','CNY',0);
insert into account_info(customer_no,account_no,account_password,account_ccy,account_balance) values('001','1002','111111','USD',1);
-- ----------------------------
-- Table structure for account_info 账户冻结信息表
-- ----------------------------
DROP TABLE IF EXISTS `account_freeze_info`;
CREATE TABLE `account_freeze_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `freeze_no` bigint(20) DEFAULT NULL COMMENT '冻结编号，每生成一笔冻结记录时生成此编号',
  `account_no` varchar(10) DEFAULT NULL COMMENT '账号',
  `freeze_amount` double DEFAULT NULL COMMENT '冻结金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
````
#### 以tcc-demo-bank1为例介绍一下工程的代码结构
```properties 1
Bank1TccServer为服务启动入口程序
Bank1Controller为请求访问入口程序
AccountInfoDao和AccountFreezeInfoDao分别是账户信息表和冻结账户信息持久化对象
AccountInfoServiceImpl为外汇交易业务实现类
```
业务处理处理一共有三步，分别是try,confirm和cancel  
下面以这个业务场景介绍代码的处理：  
1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；  
2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元。  
```properties 3
在try那一步做了以下操作：
1.针对A的美元账户生成一笔冻结记录；
2.将A的美元账户的账户余额减1；
3.调用微服务tcc-demo-bank2，执行B账户的人民币兑换美元操作

在confirm那一步做了以下操作：
1.将A账户的人民币账户的账户余额加7；
2.删除在try那一步生成的冻结记录;

在cancel那一步做了以下操作：
当出现异常时，会调用此方法进行回滚
```

### 下面对tcc-demo-bank2做一下介绍
代码结构基本和tcc-demo-bank1一致；  
业务处理类AccountInfoServiceImpl的业务逻辑，同样是分为try,confirm和cancel三步
```properties 2
在try那一步做了以下操作：
1.针对B的人民币账户生成一笔冻结记录；
2.将B的人民币账户的账户余额减7；

在confirm那一步做了以下操作：
1.将B账户的美元账户的账户余额加1；
2.删除在try那一步生成的冻结记录;

在cancel那一步做了以下操作：
当出现异常时，会调用此方法进行回滚
```
