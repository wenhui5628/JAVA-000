package com.geek.homework.week5;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/***
 * 使用JDBC和PreparedStatement方式，操作数据库，加上事务处理，批处理，以及使用Hikari连接池获取连接
 */
public class JdbcWithHikari {
    public static void main(String[] args){
        // 如何获得属性文件的输入流？
        // 通常情况下使用类的加载器的方式进行获取：
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try (InputStream is = JdbcWithHikari.class.getClassLoader().getResourceAsStream("hikari.properties")) {
            // 加载属性文件并解析：
            Properties props = new Properties();
            props.load(is);
            HikariConfig config = new HikariConfig(props);
            HikariDataSource sHikariDataSource = new HikariDataSource(config);
            connection = sHikariDataSource.getConnection();
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
