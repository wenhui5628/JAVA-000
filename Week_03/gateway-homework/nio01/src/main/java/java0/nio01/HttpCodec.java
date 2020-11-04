package java0.nio01;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class HttpCodec {
    ByteBuffer byteBuffer;

    public HttpCodec(){
        //申请足够大的内存记录读取的数据 (一行)
        byteBuffer = ByteBuffer.allocate(10 * 1024);
    }
    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String readLine(InputStream inputStream) throws IOException {
        try {
            byte b;
            boolean isMabeyEofLine = false;
            //标记
            byteBuffer.clear();
            byteBuffer.mark();
            while ((b = (byte) inputStream.read()) != -1) {
                byteBuffer.put(b);
                // 读取到/r则记录，判断下一个字节是否为/n
                if (b == HttpConst.CR) {
                    isMabeyEofLine = true;
                } else if (isMabeyEofLine) {
                    //上一个字节是/r 并且本次读取到/n
                    if (b == HttpConst.LF) {
                        //获得目前读取的所有字节
                        byte[] lineBytes = new byte[byteBuffer.position()];
                        //返回标记位置
                        byteBuffer.reset();
                        byteBuffer.get(lineBytes);
                        //清空所有index并重新标记
                        byteBuffer.clear();
                        byteBuffer.mark();
                        return new String(lineBytes);
                    }
                    isMabeyEofLine = false;
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
        throw new IOException("Response Read Line.");
    }

    /**
     * 用于解析头部
     * @param inputStream
     * @return
     * @throws IOException
     */
    public Map<String,String> readHeaders(InputStream inputStream) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        while (true) {
            String line = readLine(inputStream);
            //读取到空行 则下面的为body
            if (isEmptyLine(line)) {
                break;
            }
            int index = line.indexOf(":");
            if (index > 0) {
                String name = line.substring(0, index);
                // ": "移动两位到 总长度减去两个("\r\n")
                String value = line.substring(index + 2, line.length() - 2);
                headers.put(name, value);
            }
        }
        return headers;
    }

    public byte[] readBytes(InputStream inputStream, int length) throws IOException {
        byte[] bytes=new byte[length];
        int readNum=0;
        while (true){
            readNum +=inputStream.read(bytes,readNum,length-readNum);
            //读取完毕
            if(readNum==length){
                return  bytes;
            }
        }
    }

    private boolean isEmptyLine(String line) {
        return line==null || line.equals("\r\n");
    }

    /**
     * 解析分块编码形式的响应体
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String readChunked(InputStream inputStream) throws IOException {
        int len=-1;
        boolean isEmptyData=false;
        StringBuffer buffer=new StringBuffer();
        while (true){
            if(len<0){
                String line= readLine(inputStream);
                //减掉\r\n
                line=line.substring(0,line.length()-2);
                //Chunked 编码最后一段数据为0 \r\n\r\n
                len=Integer.valueOf(line,16);
                isEmptyData=len==0;
            }else {
                //块的长度不包括\r\n 所以加2 将\r\n读走
                byte[] bytes=readBytes(inputStream,len+2);
                buffer.append(new String(bytes));
                len=-1;
                if(isEmptyData){
                    return buffer.toString();
                }
            }
        }
    }
}
