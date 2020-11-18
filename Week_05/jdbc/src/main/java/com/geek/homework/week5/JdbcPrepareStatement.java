package com.geek.homework.week5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/***
 * 使用JDBC和PreparedStatement方式，操作数据库，并加上事务处理以及批处理
 */
public class JdbcPrepareStatement {
    public static void main(String[] args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","123456");
            connection.setAutoCommit(false);
            String insertSql = "insert into user(id,username) values(?,?)";
            preparedStatement = connection.prepareStatement(insertSql);
            for(int i=1;i<= 10;i++){
                preparedStatement.setInt(1,i);
                preparedStatement.setString(2,"name"+i);
                preparedStatement.addBatch();
//                System.out.println("===执行成功，影响记录数为:"+preparedStatement.executeUpdate());
            }
            preparedStatement.executeBatch();
//            String createUsersSql = "update user set username = 'xiaohui2222' where age = 10";
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
            throw new RuntimeException(e);
        }finally {
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
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
