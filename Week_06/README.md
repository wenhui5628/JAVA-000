### 本周作业完成情况
#### （必做）：基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL 的 SQL 文件到 Github（后面2周的作业依然要是用到这个表结构）。
#### 以下是我设计的数据库表ER图
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_06/%E7%94%B5%E5%95%86%E4%BA%A4%E6%98%93%E5%9C%BA%E6%99%AF%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1.jpg)

#### 表清单如下：
      用户信息表:customer_info
      商品分类:product_category
      商品信息表:product_info
      订单表:order_master
      订单明细:order_detail
      支付信息:payment_info

#### 各表之间关系说明如下：
      customer_info和order_master：
      customer_info ---> order_master : 一个用户可以创建多个订单，一对多
      order_master ---> customer_info : 一个订单只有一个用户创建，一对一

      order_master和order_detail：
      order_master ---> order_detail : 一个订单可以包含多个订单明细，因为一个订单可以购买多个商品，每个商品的购买信息在order_detail表中记录，一对多
      order_detail ---> order_master : 一个订单明细只能包含在一个订单中， 一对一

      order_detail 和 product_info：
      order_detail ---> product_info : 一个订单明细只对应一个商品信息，一对一
      product_info ---> order_detail : 一个商品包含在多个订单明细中，一对多

      product_info 和 product_category：
      product_info ---> product_category : 一件商品属于一种商品类别，一对一
      product_category ---> product_info : 一种商品类别包含多个商品，一对多

      order_master 和 payment_info：
      order_master ---> payment_info : 一个订单对应一笔支付信息，一对一
      payment_info ---> order_master : 一笔支付信息对应一个订单，一对一
     
 #### 详细的建表语句如下：
       /*==============================================================*/
      /* DBMS name:      MySQL 5.0                                    */
      /* Created on:     2020/11/26 0:39:54                           */
      /*==============================================================*/


      drop table if exists customer_info;

      drop table if exists order_detail;

      drop table if exists order_master;

      drop table if exists payment_info;

      drop table if exists product_category;

      drop table if exists product_info;

      /*==============================================================*/
      /* Table: customer_info                                         */
      /*==============================================================*/
      create table customer_info
      (
         customer_id          int not null auto_increment comment '用户ID',
         login_name           varchar(20) not null comment '登录名',
         password             char(32) not null comment '用户密码',
         customer_name        varchar(30) not null comment '用户姓名',
         telephone            varchar(60) not null comment '用户电话',
         address              varchar(500) not null comment '用户地址',
         role                 tinyint not null default 0 comment '用户角色，0表示普通用户，1表示管理员',
         status               tinyint not null default 0 comment '用户状态，0表示可用，1表示不可用',
         create_time          datetime not null default CURRENT_TIMESTAMP comment '用户信息创建时间',
         update_time          datetime comment '用户信息更新时间',
         primary key (customer_id)
      );

      alter table customer_info comment '用户信息表';

      /*==============================================================*/
      /* Table: order_detail                                          */
      /*==============================================================*/
      create table order_detail
      (
         order_detail_id      int not null auto_increment comment '订单明细id',
         order_id             int not null comment '订单id',
         product_id           int not null comment '商品id',
         create_time          datetime not null comment '创建时间',
         update_time          datetime comment '更新时间',
         primary key (order_detail_id)
      );

      alter table order_detail comment '订单明细';

      /*==============================================================*/
      /* Table: order_master                                          */
      /*==============================================================*/
      create table order_master
      (
         order_id             int not null auto_increment comment '订单id',
         order_code           bigint not null comment '订单编号',
         customer_id          int not null comment '用户id',
         order_money          decimal(8,2) not null comment '订单金额',
         order_status         tinyint not null comment '订单状态，0表示生效，1表示失效',
         address              varchar(300) comment '收货地址',
         create_time          datetime not null comment '订单创建时间',
         update_time          datetime comment '订单更新时间',
         primary key (order_id)
      );

      alter table order_master comment '订单信息表';

      /*==============================================================*/
      /* Table: payment_info                                          */
      /*==============================================================*/
      create table payment_info
      (
         pay_id               int not null auto_increment comment '支付信息id',
         customer_id          int not null comment '用户id',
         order_id             int not null comment '订单id',
         pay_jrn_no           bigint not null comment '支付流水号',
         pay_status           tinyint not null default 0 comment '支付状态，0表示未支付，1表示已支付',
         create_time          datetime not null comment '创建时间',
         update_time          datetime comment '更新时间',
         primary key (pay_id)
      );

      alter table payment_info comment '支付信息';

      /*==============================================================*/
      /* Table: product_category                                      */
      /*==============================================================*/
      create table product_category
      (
         category_id          int not null auto_increment comment '商品分类id',
         parent_id            int not null comment '商品分类父id，为0表示最顶层分类，当商品有多级分类信息时，使用此id关联到上一级商品分类信息',
         category_name        varchar(30) not null comment '商品分类名称',
         category_desc        varchar(300) comment '商品分类描述',
         create_time          datetime not null default CURRENT_TIMESTAMP comment '商品分类创建时间',
         update_time          datetime comment '商品分类更新时间',
         primary key (category_id)
      );

      alter table product_category comment '记录商品分类信息';

      /*==============================================================*/
      /* Table: product_info                                          */
      /*==============================================================*/
      create table product_info
      (
         product_id           int not null auto_increment comment '商品信息id',
         product_core         char(16) not null comment '商品编码',
         product_name         varchar(50) not null comment '商品名称',
         product_desc         varchar(300) comment '商品描述',
         price                decimal(8,2) not null comment '商品价格',
         production_date      datetime not null comment '商品生产日期',
         shelf_life           int not null comment '商品有效期',
         category_id          int not null comment '商品分类id',
         publish_status       tinyint not null comment '上下架状态：0下架1上架',
         indate               datetime not null comment '商品录入时间',
         update_time          datetime comment '商品更新时间',
         primary key (product_id)
      );

      alter table product_info comment '商品信息表';

