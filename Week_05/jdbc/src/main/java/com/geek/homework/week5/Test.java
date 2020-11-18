package com.geek.homework.week5;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Test {
    public static void main(String[] args){
//        Connection connection = null;
//        Statement statement = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            String url = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
//            connection = DriverManager.getConnection(url,"root","123456");
//            statement = connection.createStatement();
//            String createUsersSql = "update user set username = 'xiaohui111' where id = 3";
//            boolean result = statement.execute(createUsersSql);
//            if(result){
//                System.out.println("===执行成功");
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            if (statement != null){
//                try {
//                    statement.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(connection!=null){
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        // 如何获得属性文件的输入流？
        // 通常情况下使用类的加载器的方式进行获取：
        Statement statement = null;
        try (InputStream is = Test.class.getClassLoader().getResourceAsStream("hikari.properties")) {
            // 加载属性文件并解析：
            Properties props = new Properties();
            props.load(is);
            HikariConfig config = new HikariConfig(props);
            HikariDataSource sHikariDataSource = new HikariDataSource(config);
            Connection connection = sHikariDataSource.getConnection();
            statement = connection.createStatement();
            String createUsersSql = "update user set username = 'xiaohui11133' where id = 3";
            boolean result = statement.execute(createUsersSql);
            if(result){
                System.out.println("===执行成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
