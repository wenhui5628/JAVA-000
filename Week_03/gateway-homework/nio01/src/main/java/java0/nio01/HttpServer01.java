package java0.nio01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

public class HttpServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                service(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            HttpCodec httpCodec = new HttpCodec();
            System.out.println(httpCodec.readLine(socket.getInputStream()));
            Map<String,String> headers = httpCodec.readHeaders(socket.getInputStream());
            Iterator<Map.Entry<String,String>> it = headers.entrySet().iterator();
            System.out.println("====头部信息====");
            while(it.hasNext()){
                Map.Entry<String,String> entry = it.next();
                System.out.println("key="+entry.getKey()+" and value="+entry.getValue());
            }
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio1";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}