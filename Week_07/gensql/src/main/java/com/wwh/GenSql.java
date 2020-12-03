package com.wwh;

import java.io.FileWriter;
import java.io.IOException;

public class GenSql {
    private static String filePath = "D:\\batchInsert.sql";

    private static void saveAsFileWriter(String content) throws IOException{
        try(FileWriter fwriter = new FileWriter(filePath, true)){
            fwriter.write(content);
        }
    }

    public static void main(String[] args) throws IOException {
        for(int i=0;i<1000000;i++){
            String sql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time) values(1,1,1,0,'address',now(),now());"+"\r\n";
            saveAsFileWriter(sql);
        }
    }
}
