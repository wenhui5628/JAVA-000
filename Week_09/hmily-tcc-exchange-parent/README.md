## 作业说明
2、（必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github：
1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；
2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元；
3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。

### 业务场景分析，见下图：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_09/%E4%BA%A4%E6%98%93%E6%A8%A1%E5%9E%8B%E5%88%86%E6%9E%90.png)

### 项目结构说明
工程分为三个模块，分别是
tcc-demo-bank1  
tcc-demo-bank2  
eureka-test  
其中