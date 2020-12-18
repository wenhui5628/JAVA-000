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
insert into customer_info(customer_no,customer_name) values('002','B');

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
insert into account_info(customer_no,account_no,account_password,account_ccy,account_balance) values('002','1003','111111','CNY',7);
insert into account_info(customer_no,account_no,account_password,account_ccy,account_balance) values('002','1004','111111','USD',0);

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