package com.geek.homework.week5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/***
 * 使用JDBC和Statement方式操作数据库，并启用事务
 */
public class JdbcWithTransaction {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url, "root", "123456");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            //执行前balance为90
            String createUsersSql = "update user set balance = 70 where id = 1";
            System.out.println("===执行成功，影响记录数为:" + statement.executeUpdate(createUsersSql));

            if (true) {
                throw new RuntimeException("系统异常");
            }

            String createUsersSq2 = "update user set balance = 50 where id = 1";
            System.out.println("===执行成功，影响记录数为:" + statement.executeUpdate(createUsersSq2));
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
