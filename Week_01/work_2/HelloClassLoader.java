package com.geek.home.wwh.week1;

import java.io.*;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    private String path;

    public HelloClassLoader(String path){
        this.path = path;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        String path = "D:/geek";
        String name = "Hello";
        Class<?> helloClass = new HelloClassLoader(path).findClass(name);
        try {
            Object obj = helloClass.newInstance();
            Method method = helloClass.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] data = new byte[0];
        String fullPath = path  + File.separator + className.replaceAll("\\.", "/") + ".xlass";
        try {
            data = readBytes(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(className, data, 0, data.length);
    }

    /***
     * 读取文件字节码
     */
    private byte[] readBytes(String fullPath) throws IOException {
        File file = new File(fullPath);
        InputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] bytes = new byte[1024];
        byte[] readBytes = null;
        try {
            bos = new ByteArrayOutputStream();
            fis = new FileInputStream(file);

            int index = 0;
            while (true) {
                int read = fis.read();
                if(read != -1){
                    bytes[index] = (byte)(255 - read);  //根据公式x=255-x 对每个字节进行处理
                    index ++;
                }else{
                    break;
                }
            }
            bos.write(bytes, 0, index);
            readBytes = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return readBytes;
    }
}
