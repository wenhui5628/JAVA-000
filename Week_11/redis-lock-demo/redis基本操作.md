## Redis基本操作
### 下载并运行redis，映射到宿主机端口
docker run -dit --name redis -p 6379:6379 redis
docker run -itd --name redis -p 6379:6379 redis --appendonly yes --protected-mode no

### 查看运行日志
docker logs -f redis

### 本地登录redis
docker exec -it redis /bin/bash

### 查看容器名称
docker ps

### 查看docker容器ip
docker inspect redis

### 使用客户端连接redis
docker exec -ti redis redis-cli -h 172.17.0.2 -p 6379

### redis默认建立了16个库，从0到15
用select xxx访问其中一个库，比如select 0

### Redis性能测试命令
redis-benchmark -n 100000 -c 32 -t SET,GET,INCR,HSET,LPUSH,MSET -q
测试结果如下：
```shell
root@9d0f97f0d79a:/data# redis-benchmark -n 100000 -c 32 -t SET,GET,INCR,HSET,LPUSH,MSET -q
SET: 12812.30 requests per second
GET: 11301.99 requests per second
INCR: 9103.32 requests per second
LPUSH: 10620.22 requests per second
HSET: 12567.55 requests per second
MSET (10 keys): 11613.05 requests per second
```

### redis基本赋值语句
```shell
$ docker exec -ti redis redis-cli -h 172.17.0.2 -p 6379
172.17.0.2:6379> set wuwenhui 88
OK
172.17.0.2:6379> set name wuwenhui
OK
172.17.0.2:6379> get name
"wuwenhui"
172.17.0.2:6379> get wuwenhui
"88"
172.17.0.2:6379> exists wuwenhui
(integer) 1
172.17.0.2:6379> exists wuwnehui2
(integer) 0
172.17.0.2:6379> append name -1988
(integer) 13
172.17.0.2:6379> get name
172.17.0.2:6379> incr wuwenhui
(integer) 89
```
### 消息发布订阅
Pub-Sub 模拟队列  
订阅：
```shell
172.17.0.2:6379> subscribe comments
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "comments"
3) (integer) 1
1) "message"
2) "comments"
3) "java"
1) "message"
2) "comments"
3) "test"
```

发布：
```shell
172.17.0.2:6379> publish comments java
(integer) 1
172.17.0.2:6379> publish comments test
(integer) 1
172.17.0.2:6379>
```

### 分布式锁
1、获取锁--单个原子性操作
```shell
 SET dlock my_random_value NX PX 30000 
```

2、释放锁--lua脚本-保证原子性+单线程，从而具有事务性 
````shell
if redis.call("get",KEYS[1]) == ARGV[1] then 
  return redis.call("del",KEYS[1]) 
else
  return 0 
end
````
