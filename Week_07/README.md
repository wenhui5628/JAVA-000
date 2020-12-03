### 作业完成情况
#### （必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
##### 采用以下几种方式测试插入效率
##### 1、用java的Statement方式逐笔插入，代码如下：
      Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","");
            statement = connection.createStatement();
            System.out.println("导入数据开始!");
            long start=System.currentTimeMillis();
            for(int i=1;i<= 1000000;i++){
                String insertSql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time)  
                values(1,1,1,0,'address',now(),now())";
                statement.executeUpdate(insertSql);
            }
            System.out.println("导入数据结束,使用时间："+ (System.currentTimeMillis()-start) + " ms");

#### （必做）读写分离-动态切换数据源版本1.0
#### （必做）读写分离-数据库框架版本2.0
