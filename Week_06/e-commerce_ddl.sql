/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/11/26 0:24:51                           */
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
   customer_id          int not null auto_increment comment '�û�ID',
   login_name           varchar(20) not null comment '��¼��',
   password             char(32) not null comment '�û�����',
   customer_name        varchar(30) not null comment '�û�����',
   telephone            varchar(60) not null comment '�û��绰',
   address              varchar(500) not null comment '�û���ַ',
   role                 tinyint not null default 0 comment '�û���ɫ��0��ʾ��ͨ�û���1��ʾ����Ա',
   status               tinyint not null default 0 comment '�û�״̬��0��ʾ���ã�1��ʾ������',
   create_time          datetime not null default CURRENT_TIMESTAMP comment '�û���Ϣ����ʱ��',
   update_time          datetime comment '�û���Ϣ����ʱ��',
   primary key (customer_id)
);

alter table customer_info comment '�û���Ϣ��';

/*==============================================================*/
/* Table: order_detail                                          */
/*==============================================================*/
create table order_detail
(
   order_detail_id      int not null auto_increment comment '������ϸid',
   order_id             int not null comment '����id',
   product_id           int not null comment '��Ʒid',
   create_time          datetime not null comment '����ʱ��',
   update_time          datetime comment '����ʱ��',
   primary key (order_detail_id)
);

alter table order_detail comment '������ϸ';

/*==============================================================*/
/* Table: order_master                                          */
/*==============================================================*/
create table order_master
(
   order_id             int not null auto_increment comment '����id',
   order_code           bigint not null comment '�������',
   customer_id          int not null comment '�û�id',
   order_money          decimal(8,2) not null comment '�������',
   order_status         tinyint not null comment '����״̬��0��ʾ��Ч��1��ʾʧЧ',
   address              varchar(300) comment '�ջ���ַ',
   create_time          datetime not null comment '��������ʱ��',
   update_time          datetime comment '��������ʱ��',
   primary key (order_id)
);

alter table order_master comment '������Ϣ��';

/*==============================================================*/
/* Table: payment_info                                          */
/*==============================================================*/
create table payment_info
(
   pay_id               int not null auto_increment comment '֧����Ϣid',
   user_id              int not null comment '�û�id',
   order_id             int not null comment '����id',
   pay_jrn_no           bigint not null comment '֧����ˮ��',
   pay_status           tinyint not null default 0 comment '֧��״̬��0��ʾδ֧����1��ʾ��֧��',
   create_time          datetime not null comment '����ʱ��',
   update_time          datetime comment '����ʱ��',
   primary key (pay_id)
);

alter table payment_info comment '֧����Ϣ';

/*==============================================================*/
/* Table: product_category                                      */
/*==============================================================*/
create table product_category
(
   category_id          int not null auto_increment comment '��Ʒ����id',
   parent_id            int not null comment '��Ʒ���ุid��Ϊ0��ʾ�����࣬����Ʒ�ж༶������Ϣʱ��ʹ�ô�id��������һ����Ʒ������Ϣ',
   category_name        varchar(30) not null comment '��Ʒ��������',
   category_desc        varchar(300) comment '��Ʒ��������',
   create_time          datetime not null default CURRENT_TIMESTAMP comment '��Ʒ���ഴ��ʱ��',
   update_time          datetime comment '��Ʒ�������ʱ��',
   primary key (category_id)
);

alter table product_category comment '��¼��Ʒ������Ϣ';

/*==============================================================*/
/* Table: product_info                                          */
/*==============================================================*/
create table product_info
(
   product_id           int not null auto_increment comment '��Ʒ��Ϣid',
   product_core         char(16) not null comment '��Ʒ����',
   product_name         varchar(50) not null comment '��Ʒ����',
   product_desc         varchar(300) comment '��Ʒ����',
   price                decimal(8,2) not null comment '��Ʒ�۸�',
   production_date      datetime not null comment '��Ʒ��������',
   shelf_life           int not null comment '��Ʒ��Ч��',
   category_id          int not null comment '��Ʒ����id',
   publish_status       tinyint not null comment '���¼�״̬��0�¼�1�ϼ�',
   indate               datetime not null comment '��Ʒ¼��ʱ��',
   update_time          datetime comment '��Ʒ����ʱ��',
   primary key (product_id)
);

alter table product_info comment '��Ʒ��Ϣ��';

