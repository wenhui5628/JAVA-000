作业要求：


一、使用GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。

1、串行GC：

-Xms128m -Xmx128m的情况分析，命令如下：

java -XX:+UseSerialGC -Xms128m -Xmx128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis

运行结果：

![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E4%B8%B2%E8%A1%8CGC-128.PNG)

可以看到，当初始堆内存和最大堆内存都设置为128m的情况下，使用串行GC，会触发很多次FULL GC直到堆内存溢出，从图中可以看到，在执行第一次FULL GC前的一次Young GC时，
Young GC完成后，新生代内存空间已被撑满DefNew:39295K->39295K(39296k)，新生代无法给新的对象分配内存空间，同时满足大对象条件，所以很多正在使用的对象被放到老年代，同时对老年代空间做了垃圾清理，老年代空间使用情况从74689K->86974K(87424K),执行了垃圾清理后老年代空间使用情况反而增加，说明老年代正在使用的对象一直在增加并且没有被释放，所以无法被回收，接下来触发了很多次FULL GC，发现都无法回收老年代的对象，最终老年代空间被新增的老年代对象撑满，发生了OOM

-Xms256m -Xmx256m的情况分析，命令如下：

java -XX:+UseSerialGC -Xms256m -Xmx256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis


二、使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例。


三、写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 Github。
采用了两种方式分别写了客户端代码，如下：
HttpClient方式：


public class HttpClientTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void request(String url){
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);

        // 获得Http客户端
        // 由客户端执行(发送)Get请求
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                log.info("响应内容长度为:" + responseEntity.getContentLength());
                log.info("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (Exception e) {
            log.error("发送报文到服务端出错，错误信息为:"+e);
        }
    }

    public static void main(String[] args){
        new HttpClientTest().request("http://localhost:8801/");
    }
}


OkHttp方式；

public class OkHttpClientTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void request(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            log.info("响应内容为："+response.body().string());
        }catch(Exception e){
            log.error("发送到服务端出错，错误信息为:"+e);
        }
    }

    public static void main(String[] args){
        new OkHttpClientTest().request("http://localhost:8801/");
    }
}

