package com.geek.homework.week5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/***
 * 使用JDBC和Statement方式操作数据库
 */
public class SimpleJdbc {
    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","123456");
            statement = connection.createStatement();
            for(int i=1;i<= 10;i++){
                String insertSql = "insert into user(id,username) values(" + i + ",'"+"name"+ i +"')";
                System.out.println("===执行成功，影响记录数为:"+statement.executeUpdate(insertSql));
            }
//            String createUsersSql = "update user set username = 'xiaohui2222' where age = 10";

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
