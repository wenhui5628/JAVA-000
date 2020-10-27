作业要求：
——

一、使用GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。
1、串行GC：
-Xms128m -Xmx128m的情况分析，命令如下：
java -XX:+UseSerialGC -Xms128m -Xmx128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis
运行结果：
D:\geek\第二周JVM和NIO\JVM>java -XX:+UseSerialGC -Xms128m -Xmx128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis
正在执行...
2020-10-28T02:57:13.742+0800: [GC (Allocation Failure) [DefNew: 34944K->4352K(39296K), 0.0071889 secs] 34944K->10242K(126720K), 0.0080011 secs] [Times: user=0.00 sys=0.00, real=0.0
1 secs]
2020-10-28T02:57:13.764+0800: [GC (Allocation Failure) [DefNew: 38762K->4351K(39296K), 0.0103351 secs] 44652K->19820K(126720K), 0.0111042 secs] [Times: user=0.01 sys=0.00, real=0.0
1 secs]
2020-10-28T02:57:13.787+0800: [GC (Allocation Failure) [DefNew: 39174K->4346K(39296K), 0.0124315 secs] 54643K->34541K(126720K), 0.0127405 secs] [Times: user=0.00 sys=0.02, real=0.0
1 secs]
2020-10-28T02:57:13.808+0800: [GC (Allocation Failure) [DefNew: 39290K->4351K(39296K), 0.0080895 secs] 69485K->44475K(126720K), 0.0088608 secs] [Times: user=0.00 sys=0.02, real=0.0
1 secs]
2020-10-28T02:57:13.824+0800: [GC (Allocation Failure) [DefNew: 39295K->4351K(39296K), 0.0104690 secs] 79419K->57137K(126720K), 0.0107792 secs] [Times: user=0.00 sys=0.02, real=0.0
1 secs]
2020-10-28T02:57:13.843+0800: [GC (Allocation Failure) [DefNew: 38796K->4348K(39296K), 0.0079791 secs] 91583K->67992K(126720K), 0.0084486 secs] [Times: user=0.02 sys=0.00, real=0.0
1 secs]
2020-10-28T02:57:13.858+0800: [GC (Allocation Failure) [DefNew: 38976K->4351K(39296K), 0.0088635 secs] 102619K->79041K(126720K), 0.0095522 secs] [Times: user=0.00 sys=0.02, real=0.
01 secs]
2020-10-28T02:57:13.875+0800: [GC (Allocation Failure) [DefNew: 39295K->39295K(39296K), 0.0006575 secs][Tenured: 74689K->86974K(87424K), 0.0227555 secs] 113985K->89949K(126720K), [
Metaspace: 2605K->2605K(1056768K)], 0.0248623 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
2020-10-28T02:57:13.908+0800: [Full GC (Allocation Failure) [Tenured: 87401K->87374K(87424K), 0.0180069 secs] 126696K->99393K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.01894
50 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]
2020-10-28T02:57:13.934+0800: [Full GC (Allocation Failure) [Tenured: 87374K->87339K(87424K), 0.0166647 secs] 126450K->105216K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0174
665 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
2020-10-28T02:57:13.957+0800: [Full GC (Allocation Failure) [Tenured: 87339K->87264K(87424K), 0.0243701 secs] 126597K->106787K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0255
818 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
2020-10-28T02:57:13.986+0800: [Full GC (Allocation Failure) [Tenured: 87408K->87408K(87424K), 0.0055778 secs] 126615K->114154K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0063
074 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2020-10-28T02:57:13.998+0800: [Full GC (Allocation Failure) [Tenured: 87408K->87408K(87424K), 0.0071481 secs] 126654K->116420K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0080
096 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.008+0800: [Full GC (Allocation Failure) [Tenured: 87408K->87408K(87424K), 0.0058998 secs] 126660K->119326K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0065
858 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.017+0800: [Full GC (Allocation Failure) [Tenured: 87408K->87243K(87424K), 0.0254331 secs] 126665K->116368K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0262
304 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
2020-10-28T02:57:14.046+0800: [Full GC (Allocation Failure) [Tenured: 87377K->87377K(87424K), 0.0075527 secs] 126665K->120631K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0087
379 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.063+0800: [Full GC (Allocation Failure) [Tenured: 87377K->87377K(87424K), 0.0119494 secs] 126485K->123902K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0132
243 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.082+0800: [Full GC (Allocation Failure) [Tenured: 87377K->87377K(87424K), 0.0036931 secs] 126534K->125121K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0044
278 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T02:57:14.101+0800: [Full GC (Allocation Failure) [Tenured: 87377K->87324K(87424K), 0.0301823 secs] 126282K->123453K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0308
074 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
2020-10-28T02:57:14.147+0800: [Full GC (Allocation Failure) [Tenured: 87324K->87324K(87424K), 0.0064254 secs] 126485K->124534K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0070
921 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.169+0800: [Full GC (Allocation Failure) [Tenured: 87324K->87324K(87424K), 0.0023311 secs] 126154K->125359K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0029
703 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T02:57:14.193+0800: [Full GC (Allocation Failure) [Tenured: 87324K->87324K(87424K), 0.0043213 secs] 126361K->126144K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0050
285 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T02:57:14.205+0800: [Full GC (Allocation Failure) [Tenured: 87324K->87390K(87424K), 0.0256881 secs] 126485K->126064K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0266
893 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
2020-10-28T02:57:14.244+0800: [Full GC (Allocation Failure) [Tenured: 87390K->87390K(87424K), 0.0055332 secs] 126428K->125861K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0063
638 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.258+0800: [Full GC (Allocation Failure) [Tenured: 87390K->87390K(87424K), 0.0049818 secs] 126231K->126137K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0056
947 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2020-10-28T02:57:14.277+0800: [Full GC (Allocation Failure) [Tenured: 87390K->87390K(87424K), 0.0029564 secs] 126665K->126665K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0036
121 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T02:57:14.296+0800: [Full GC (Allocation Failure) [Tenured: 87390K->87359K(87424K), 0.0258480 secs] 126665K->126634K(126720K), [Metaspace: 2606K->2606K(1056768K)], 0.0264
816 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:45)
        at GCLogAnalysis.main(GCLogAnalysis.java:25)
Heap
 def new generation   total 39296K, used 39289K [0x00000000f8000000, 0x00000000faaa0000, 0x00000000faaa0000)
  eden space 34944K, 100% used [0x00000000f8000000, 0x00000000fa220000, 0x00000000fa220000)
  from space 4352K,  99% used [0x00000000fa660000, 0x00000000faa9e490, 0x00000000faaa0000)
  to   space 4352K,   0% used [0x00000000fa220000, 0x00000000fa220000, 0x00000000fa660000)
 tenured generation   total 87424K, used 87359K [0x00000000faaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 87424K,  99% used [0x00000000faaa0000, 0x00000000fffefec8, 0x00000000ffff0000, 0x0000000100000000)
 Metaspace       used 2636K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 285K, capacity 386K, committed 512K, reserved 1048576K



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

