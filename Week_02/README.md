
作业要求：


一、使用GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。

1、串行GC  

-Xms128m -Xmx128m的情况分析，命令如下：  
java -XX:+UseSerialGC -Xms128m -Xmx128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis   

运行结果：  
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E4%B8%B2%E8%A1%8CGC-128.PNG)  
可以看到，当初始堆内存和最大堆内存都设置为128m的情况下，使用串行GC，会触发很多次FULL GC直到堆内存溢出，从图中可以看到，在执行第一次FULL GC前的一次Young GC时，
Young GC完成后，新生代内存空间已被撑满DefNew:39295K->39295K(39296k)，新生代无法给新的对象分配内存空间，同时满足大对象条件，所以很多正在使用的对象被放到老年代，同时对老年代空间做了垃圾清理，老年代空间使用情况从74689K->86974K(87424K),执行了垃圾清理后老年代空间使用情况反而增加，说明老年代正在使用的对象一直在增加并且没有被释放，所以无法被回收，接下来触发了很多次FULL GC，发现都无法回收老年代的对象，最终老年代空间被新增的老年代对象撑满，发生了OOM

-Xms256m -Xmx256m的情况分析，命令如下：  
java -XX:+UseSerialGC -Xms256m -Xmx256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis  

运行结果：  
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E4%B8%B2%E8%A1%8CGC-256.png)  
可以看到，当初始堆内存和最大堆内存都设置为256m的情况下，跟128m的情况类似，也是新生代已经无法正常进行垃圾清理，最后一次Young GC的情况：DefNew: 69952K->69952K(78656K)，Tenured: 174513K->174666K(174784K)，这时老年代也已经接近占满，触发了FULL GC，从图中可以看到，触发了很多次FULL GC，但是也无法正常回收内存，不管是老年代内存：Tenured: 174754K->174545K(174784K)，还是整个堆空间内存：253408K->244124K(253440K)，都持续处于满载状态，这个时候虽然没有发生OOM，但是实际上也已经无法通过FULL GC回收内存，跟OOM的情况也差不多了，这种情况再持续运行一段时间必然也会发生OOM
最后的总结信息中也能看到新生代内存使用率为100%，老年代内存使用情况为99%  
def new generation   total 78656K, used 70436K  
eden space 69952K, 100% used  
tenured generation   total 174784K, used 174545K  
the space 174784K,  99% used  

-Xms512m -Xmx512m的情况分析，命令如下：  
java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis  

运行结果：  
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E4%B8%B2%E8%A1%8CGC-512.png)  
可以看到，当初始堆内存和最大堆内存都设置为512m的情况下，没有触发FULL GC，但是从图中可以看到，一开始Young GC回收效率还可以，到了后面执行Young GC已经无法回收内存了，DefNew: 157247K->157247K(157248K)，老年代空间使用率也越来越高，越来越多对象进入老年代Tenured: 339410K->301776K(349568K)，对象的回收比率也不高496658K->301776K，执行完成后，年轻代使用率比较低，
老年代使用率比较高，是因为很多新创建出来的对象在年轻代已经没空间存放了，直接进入了老年代，如下：  
def new generation   total 157248K, used 5836K  
eden space 139776K,   4% used  
tenured generation   total 349568K, used 316065K  
the space 349568K,  90% used  

-Xms1g -Xmx1g的情况分析，命令如下：  
java -XX:+UseSerialGC -Xms1g -Xmx1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps  GCLogAnalysis  

运行结果：  
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E4%B8%B2%E8%A1%8CGC-1g.png)  
从图中可以看到，当分配的堆内存提升到1G后，运行同样的程序发生Young GC的次数少了，但是我们可以看到，执行每次Young GC的使用时间反而比前面分配小内存的时候更多，基本上每次执行的时间都在45ms以上，而前面分配小内存的情况下，大部分时候都是10多毫秒或者20多毫秒，也就是当分配的堆内存提高到1G，并不会带来垃圾回收性能的提升，改变的只是分配给老年代的空间增大了，从而使得老年代能存放的对象更多而已。

2、并行GC  
-Xms128m -Xmx128m的情况分析，命令如下：  
java -XX:+UseParallelGC -Xms128m -Xmx128m  -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis 

运行结果：  
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E5%B9%B6%E8%A1%8CGC-128.PNG)  
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E5%B9%B6%E8%A1%8CGC-128-2.PNG)  
从图中可以看出，当堆内存分配128m时，使用并行GC也会发生内存溢出

-Xms512m -Xmx512m的情况分析，命令如下：  
java -XX:+UseParallelGC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis  

运行结果：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/%E5%B9%B6%E8%A1%8CGC-512.png)
并行GC在Young GC过程中耗时相对于串行GC低，在Full GC过程中耗时平均大约是六十多毫秒，同等条件下，比串行GC执行Full GC耗时要高，整体看来Full GC的频率越来越高

3、CMS GC
-Xms128m -Xmx128m的情况分析，命令如下：  
java -XX:+UseConcMarkSweepGC -Xms128m -Xmx128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

运行结果：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/CMS-GC-128.png)
从图中可以看出，当堆内存分配128m时，使用CMS GC也会发生内存溢出

-Xms512m -Xmx512m的情况分析，命令如下：  
java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

运行结果：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/CMS-GC-512.png)  
CMS GC收集器是一种以获取最短回收停顿时间为目标的收集器，是基于标记-清除算法实现的，运作过程相对于前面几种收集器来说要更复杂一些，
从图中可以看到，CMS GC的垃圾收回周期会经过以下几个阶段：  
阶段 1：Initial Mark（初始标记）  
阶段 2：Concurrent Mark（并发标记）  
阶段 3：Concurrent Preclean（并发预清理）  
阶段 4： Final Remark（最终标记）  
阶段 5： Concurrent Sweep（并发清除）  
阶段 6： Concurrent Reset（并发重置）  
其中初始标记、重新标记（最终标记）这两个步骤仍然需要“Stop The World”，初始标记仅仅只是标记一下GC Roots能直接关联到的对象，速度很快，图中初始标记这一步仅仅花销1ms，而并发标记就是从GC Roots的直接关联对象开始遍历整个对象图的过程，这个过程耗时较长但是不需要停顿用户线程，从图中可以看到，并发标记开始到最终标记开始期间，经历了并发标记以及并发预清理，一共消耗了188ms左右，但是这个过程不会停顿用户线程，用户线程可以和垃圾收集线程一起并发运行；而重新标记阶段则是为了修正并发标记期间，因用户程序继续运行而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间通常会比初始标记阶段稍长一些，但也远比并发标记阶段的时间短，从图中可以看到，从最终标记开始到并发清除开始这段时间，停顿了15ms左右，比并发标记和并发预清理期间耗时188ms少很多，最后是并发清除阶段，清理删除掉标记阶段判断的已经死亡的对象，由于不需要移动存活对象，所以这个阶段也是可以与用户线程同时并发的，从图中可以看到，并发清除和并发重置，一共耗时30ms左右。
由于在整个过程中耗时最长的并发标记和并发清除阶段，垃圾收集器线程都可以与用户线程一起工作，所以从总体上说，CMS收集器的内存回收过程是与用户线程一起并发执行的。


4、G1 GC

-Xms512m -Xmx512m的情况分析，命令如下:  
java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDateStamps GCLogAnalysis  

运行结果：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_02/img/G1-GC-512.png)


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

