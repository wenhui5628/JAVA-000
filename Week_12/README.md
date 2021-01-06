### 作业：
#### 1、（必做）配置redis的主从复制，sentinel高可用，Cluster集群。

#### 准备工作
1.下载redis文件：https://github.com/MicrosoftArchive/redis/releases（使用3.2.100）  
2.下载Ruby安装文件：https://rubyinstaller.org/downloads/（使用3.0.0-1）  
3.下载Ruby环境下Redis的驱动：https://rubygems.org/gems/redis/versions/3.2.2，考虑到兼容性，这里下载的是3.2.2版本  
4.下载Redis官方提供的创建Redis集群的ruby脚本文件redis-trib.rb，路径如下：https://raw.githubusercontent.com/MSOpenTech/redis/3.0/src/redis-trib.rb（全选文本复制下来，存到ruby安装目录下）  


#### Redis主从
1.把下载的redis安装包解压  
2.复制2份redis.windows.conf配置文件，分别命名为redis.windows6380.conf和redis.windows6381.conf  
3.修改配置
```text
redis.windows6380.conf如下：
port 6380
bind 127.0.0.1
slaveof 127.0.0.1 6379 // 设置master服务器为6379，如果不写这一段，也可以启动服务后，在连上服务器后执行slaveof 127.0.0.1 6379

redis.windows6381.conf如下：
port 6381
bind 127.0.0.1
slaveof 127.0.0.1 6379 // 设置master服务器为6379
```
4.启动服务  
```text
在redis安装目录下执行命令
redis-server.exe redis.windows.conf
redis-server.exe redis.windows6380.conf
redis-server.exe redis.windows6381.conf
```
结果如下:  
6379端口服务：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-server.exe redis.windows.conf
                _._
           _.-``__ ''-._
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6379
 |    `-._   `._    /     _.-'    |     PID: 124492
  `-._    `-._  `-./  _.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |           http://redis.io
  `-._    `-._`-.__.-'_.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |
  `-._    `-._`-.__.-'_.-'    _.-'
      `-._    `-.__.-'    _.-'
          `-._        _.-'
              `-.__.-'

[124492] 07 Jan 00:27:18.171 # Server started, Redis version 3.2.100
[124492] 07 Jan 00:27:18.186 * DB loaded from disk: 0.007 seconds
[124492] 07 Jan 00:27:18.186 * The server is now ready to accept connections on port 6379
```
6380端口服务：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-server.exe redis.windows6380.conf
                _._
           _.-``__ ''-._
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6380
 |    `-._   `._    /     _.-'    |     PID: 123908
  `-._    `-._  `-./  _.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |           http://redis.io
  `-._    `-._`-.__.-'_.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |
  `-._    `-._`-.__.-'_.-'    _.-'
      `-._    `-.__.-'    _.-'
          `-._        _.-'
              `-.__.-'

[123908] 07 Jan 00:29:51.462 # Server started, Redis version 3.2.100
[123908] 07 Jan 00:29:51.462 * DB loaded from disk: 0.000 seconds
[123908] 07 Jan 00:29:51.462 * The server is now ready to accept connections on port 6380
[123908] 07 Jan 00:29:51.462 * Connecting to MASTER 127.0.0.1:6379
[123908] 07 Jan 00:29:51.462 * MASTER <-> SLAVE sync started
[123908] 07 Jan 00:29:51.478 * Non blocking connect for SYNC fired the event.
[123908] 07 Jan 00:29:51.478 * Master replied to PING, replication can continue...
[123908] 07 Jan 00:29:51.478 * Partial resynchronization not possible (no cached master)
[123908] 07 Jan 00:29:51.525 * Full resync from master: d2fc9b32c7fa035fd7dd255edd132ada56e784f8:1
[123908] 07 Jan 00:29:51.754 * MASTER <-> SLAVE sync: receiving 75 bytes from master
[123908] 07 Jan 00:29:51.754 * MASTER <-> SLAVE sync: Flushing old data
[123908] 07 Jan 00:29:51.770 * MASTER <-> SLAVE sync: Loading DB in memory
[123908] 07 Jan 00:29:51.770 * MASTER <-> SLAVE sync: Finished with success
```
6381端口服务：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-server.exe redis.windows6381.conf
                _._
           _.-``__ ''-._
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6381
 |    `-._   `._    /     _.-'    |     PID: 124832
  `-._    `-._  `-./  _.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |           http://redis.io
  `-._    `-._`-.__.-'_.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |
  `-._    `-._`-.__.-'_.-'    _.-'
      `-._    `-.__.-'    _.-'
          `-._        _.-'
              `-.__.-'

[124832] 07 Jan 00:31:03.531 # Server started, Redis version 3.2.100
[124832] 07 Jan 00:31:03.531 * DB loaded from disk: 0.000 seconds
[124832] 07 Jan 00:31:03.531 * The server is now ready to accept connections on port 6381
[124832] 07 Jan 00:31:03.546 * Connecting to MASTER 127.0.0.1:6379
[124832] 07 Jan 00:31:03.546 * MASTER <-> SLAVE sync started
[124832] 07 Jan 00:31:03.546 * Non blocking connect for SYNC fired the event.
[124832] 07 Jan 00:31:03.562 * Master replied to PING, replication can continue...
[124832] 07 Jan 00:31:03.562 * Partial resynchronization not possible (no cached master)
[124832] 07 Jan 00:31:03.609 * Full resync from master: d2fc9b32c7fa035fd7dd255edd132ada56e784f8:85
[124832] 07 Jan 00:31:03.780 * MASTER <-> SLAVE sync: receiving 75 bytes from master
[124832] 07 Jan 00:31:03.828 * MASTER <-> SLAVE sync: Flushing old data
[124832] 07 Jan 00:31:03.828 * MASTER <-> SLAVE sync: Loading DB in memory
[124832] 07 Jan 00:31:03.828 * MASTER <-> SLAVE sync: Finished with success
```

5.查看主从  
通过命令连接6380redis服务：redis-cli -h 127.0.0.1 -p 6380     
查看主从信息：info Replication   
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-cli -h 127.0.0.1 -p 6380
127.0.0.1:6380> info Replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:up
master_last_io_seconds_ago:5
master_sync_in_progress:0
slave_repl_offset:253
slave_priority:100
slave_read_only:1
connected_slaves:0
master_repl_offset:0
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

#### Redis哨兵（sentinel）
1.和上面主从一样启动6379、6380、6381三个服务  
2.创建sentinel26379.conf文件，然后复制两份sentinel26380.conf、sentinel26381.conf  
sentinel26379.conf文件内容:   
```text
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 15000
```
sentinel26380.conf文件内容:  
```text
port 26380
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 15000
```
sentinel26381.conf文件内容:  
```text
port 26381
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 15000
```
3.启动哨兵服务  
```shell
redis-server.exe sentinel26379.conf --sentinel
redis-server.exe sentinel26380.conf --sentinel
redis-server.exe sentinel26381.conf --sentinel
```
启动结果如下：
26379端口：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-server.exe sentinel26379.conf --sentinel
                _._
           _.-``__ ''-._
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26379
 |    `-._   `._    /     _.-'    |     PID: 92292
  `-._    `-._  `-./  _.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |           http://redis.io
  `-._    `-._`-.__.-'_.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |
  `-._    `-._`-.__.-'_.-'    _.-'
      `-._    `-.__.-'    _.-'
          `-._        _.-'
              `-.__.-'

[92292] 07 Jan 00:47:27.770 # Sentinel ID is 03e57f02cc466cb826f332c9b65e767e3bfee414
[92292] 07 Jan 00:47:27.770 # +monitor master mymaster 127.0.0.1 6379 quorum 2
[92292] 07 Jan 00:47:27.770 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
[92292] 07 Jan 00:47:27.788 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
```
26380端口：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-server.exe sentinel26380.conf --sentinel
                _._
           _.-``__ ''-._
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26380
 |    `-._   `._    /     _.-'    |     PID: 120984
  `-._    `-._  `-./  _.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |           http://redis.io
  `-._    `-._`-.__.-'_.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |
  `-._    `-._`-.__.-'_.-'    _.-'
      `-._    `-.__.-'    _.-'
          `-._        _.-'
              `-.__.-'

[120984] 07 Jan 00:50:21.327 # Sentinel ID is bf1cdbc89d1364c8a9aae9039ca1d6f5ee353bce
[120984] 07 Jan 00:50:21.327 # +monitor master mymaster 127.0.0.1 6379 quorum 2
[120984] 07 Jan 00:50:21.327 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
[120984] 07 Jan 00:50:21.327 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
[120984] 07 Jan 00:50:21.649 * +sentinel sentinel 03e57f02cc466cb826f332c9b65e767e3bfee414 127.0.0.1 26379
7.0.0.1 6379
```
26381端口：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-server.exe sentinel26381.conf --sentinel
                _._
           _.-``__ ''-._
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26381
 |    `-._   `._    /     _.-'    |     PID: 122480
  `-._    `-._  `-./  _.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |           http://redis.io
  `-._    `-._`-.__.-'_.-'    _.-'
 |`-._`-._    `-.__.-'    _.-'_.-'|
 |    `-._`-._        _.-'_.-'    |
  `-._    `-._`-.__.-'_.-'    _.-'
      `-._    `-.__.-'    _.-'
          `-._        _.-'
              `-.__.-'

[122480] 07 Jan 00:51:36.678 # Sentinel ID is 6acc5c0d30333f3dbe92a777aea340e01b031bfa
[122480] 07 Jan 00:51:36.678 # +monitor master mymaster 127.0.0.1 6379 quorum 2
[122480] 07 Jan 00:51:36.678 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
[122480] 07 Jan 00:51:36.678 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
[122480] 07 Jan 00:51:36.875 * +sentinel sentinel 03e57f02cc466cb826f332c9b65e767e3bfee414 127.0.0.1 26379
7.0.0.1 6379
[122480] 07 Jan 00:51:37.699 * +sentinel sentinel bf1cdbc89d1364c8a9aae9039ca1d6f5ee353bce 127.0.0.1 26380
7.0.0.1 6379
```

4.关闭6379服务，查看主从变化  
6379没关闭之前，在6380服务看master是6379  
```shell
127.0.0.1:6380> info Replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:up
master_last_io_seconds_ago:5
master_sync_in_progress:0
slave_repl_offset:253
slave_priority:100
slave_read_only:1
connected_slaves:0
master_repl_offset:0
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```
关闭6379之后，可以看到master自动变成6381了  
```shell
127.0.0.1:6380> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6381
master_link_status:up
master_last_io_seconds_ago:1
master_sync_in_progress:0
slave_repl_offset:556
slave_priority:100
slave_read_only:1
connected_slaves:0
master_repl_offset:0
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```
可以看到6380端口服务的启动日志也发生了以下变化:  
```shell
[123908] 07 Jan 00:55:23.392 # CONFIG REWRITE executed with success.
[123908] 07 Jan 00:55:23.408 * 1 changes in 900 seconds. Saving...
[123908] 07 Jan 00:55:23.439 * Background saving started by pid 68984
[123908] 07 Jan 00:55:23.658 # fork operation complete
[123908] 07 Jan 00:55:23.658 * Background saving terminated with success
[123908] 07 Jan 00:55:23.876 * Connecting to MASTER 127.0.0.1:6381
[123908] 07 Jan 00:55:23.876 * MASTER <-> SLAVE sync started
[123908] 07 Jan 00:55:23.876 * Non blocking connect for SYNC fired the event.
[123908] 07 Jan 00:55:23.876 * Master replied to PING, replication can continue...
[123908] 07 Jan 00:55:23.876 * Partial resynchronization not possible (no cached master)
[123908] 07 Jan 00:55:23.908 * Full resync from master: 43fd3d65dcb607fda53bd0a450afa3ffd4b8a6c7:1
[123908] 07 Jan 00:55:24.064 * MASTER <-> SLAVE sync: receiving 75 bytes from master
[123908] 07 Jan 00:55:24.079 * MASTER <-> SLAVE sync: Flushing old data
[123908] 07 Jan 00:55:24.079 * MASTER <-> SLAVE sync: Loading DB in memory
[123908] 07 Jan 00:55:24.079 * MASTER <-> SLAVE sync: Finished with success
```
这个时候再重新启动6379,6379会自动变成6381的slave    
启动日志如下：
```shell
[115304] 07 Jan 00:59:44.388 # Server started, Redis version 3.2.100
[115304] 07 Jan 00:59:44.388 * DB loaded from disk: 0.000 seconds
[115304] 07 Jan 00:59:44.389 * The server is now ready to accept connections on port 6379
[115304] 07 Jan 01:00:05.414 * SLAVE OF 127.0.0.1:6381 enabled (user request from 'id=2 addr=127.0.0.1:651
entinel-03e57f02-cmd age=10 idle=0 flags=x db=0 sub=0 psub=0 multi=3 qbuf=0 qbuf-free=32768 obl=36 oll=0 o
 cmd=exec')
[115304] 07 Jan 01:00:05.430 # CONFIG REWRITE executed with success.
[115304] 07 Jan 01:00:06.161 * Connecting to MASTER 127.0.0.1:6381
[115304] 07 Jan 01:00:06.162 * MASTER <-> SLAVE sync started
[115304] 07 Jan 01:00:06.163 * Non blocking connect for SYNC fired the event.
[115304] 07 Jan 01:00:06.164 * Master replied to PING, replication can continue...
[115304] 07 Jan 01:00:06.174 * Partial resynchronization not possible (no cached master)
[115304] 07 Jan 01:00:06.199 * Full resync from master: 43fd3d65dcb607fda53bd0a450afa3ffd4b8a6c7:54918
[115304] 07 Jan 01:00:06.372 * MASTER <-> SLAVE sync: receiving 75 bytes from master
[115304] 07 Jan 01:00:06.377 * MASTER <-> SLAVE sync: Flushing old data
[115304] 07 Jan 01:00:06.386 * MASTER <-> SLAVE sync: Loading DB in memory
[115304] 07 Jan 01:00:06.393 * MASTER <-> SLAVE sync: Finished with success
```
通过客户端连接后输入命令info replication查看也同样看到6379变成6381的slave  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-cli.exe -h 127.0.0.1 -p 6379
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6381
master_link_status:up
master_last_io_seconds_ago:1
master_sync_in_progress:0
slave_repl_offset:66848
slave_priority:100
slave_read_only:1
connected_slaves:0
master_repl_offset:0
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```
#### Redis集群（cluster）
1.安装ruby环境  
双击下载的“rubyinstaller-3.0.0-1-x64.exe”安装即可，安装会默认把ruby添加到path环境变量    
2.安装Ruby环境下Redis的驱动  
将下载的"Ruby环境下Redis的驱动文件(redis-3.2.2.gem)"拷贝到Ruby安装根目录C:/Ruby27-x64下。  
然后执行安装命令如下：  
```shell
gem install --local C:/Ruby30-x64/redis-3.2.2.gem
Successfully installed redis-3.2.2
Parsing documentation for redis-3.2.2
Installing ri documentation for redis-3.2.2
Done installing documentation for redis after 5 seconds
1 gem installed
```
3.将下载的“创建Redis集群的ruby脚本文件redis-trib.rb”文件拷贝到Redis安装根目录(D:\geek\redis\Redis-x64-3.2.100)下。  
4.新建配置文件redis.windows-service-6380.conf  
```text
port 6380      
loglevel notice    
logfile "D:/geek/redis/Redis-x64-3.2.100/Logs/redis6380_log.txt"       
appendonly yes
appendfilename "appendonly.6380.aof"   
cluster-enabled yes                                    
cluster-config-file nodes.6380.conf
cluster-node-timeout 15000
cluster-slave-validity-factor 10
cluster-migration-barrier 1
cluster-require-full-coverage yes
```
复制5份  
redis.windows-service-6381.conf  
```text
port 6381      
loglevel notice    
logfile "D:/geek/redis/Redis-x64-3.2.100/Logs/redis6381_log.txt"       
appendonly yes
appendfilename "appendonly.6381.aof"   
cluster-enabled yes                                    
cluster-config-file nodes.6381.conf
cluster-node-timeout 15000
cluster-slave-validity-factor 10
cluster-migration-barrier 1
cluster-require-full-coverage yes
```
redis.windows-service-6382.conf  
```text
port 6382      
loglevel notice    
logfile "D:/geek/redis/Redis-x64-3.2.100/Logs/redis6382_log.txt"       
appendonly yes
appendfilename "appendonly.6382.aof"   
cluster-enabled yes                                    
cluster-config-file nodes.6382.conf
cluster-node-timeout 15000
cluster-slave-validity-factor 10
cluster-migration-barrier 1
cluster-require-full-coverage yes
```
redis.windows-service-6383.conf  
```text
port 6383      
loglevel notice    
logfile "D:/geek/redis/Redis-x64-3.2.100/Logs/redis6383_log.txt"       
appendonly yes
appendfilename "appendonly.6383.aof"   
cluster-enabled yes                                    
cluster-config-file nodes.6383.conf
cluster-node-timeout 15000
cluster-slave-validity-factor 10
cluster-migration-barrier 1
cluster-require-full-coverage yes
```
redis.windows-service-6384.conf  
```text
port 6384      
loglevel notice    
logfile "D:/geek/redis/Redis-x64-3.2.100/Logs/redis6384_log.txt"       
appendonly yes
appendfilename "appendonly.6384.aof"   
cluster-enabled yes                                    
cluster-config-file nodes.6384.conf
cluster-node-timeout 15000
cluster-slave-validity-factor 10
cluster-migration-barrier 1
cluster-require-full-coverage yes
```
redis.windows-service-6385.conf  
```text
port 6385      
loglevel notice    
logfile "D:/geek/redis/Redis-x64-3.2.100/Logs/redis6385_log.txt"       
appendonly yes
appendfilename "appendonly.6385.aof"   
cluster-enabled yes                                    
cluster-config-file nodes.6385.conf
cluster-node-timeout 15000
cluster-slave-validity-factor 10
cluster-migration-barrier 1
cluster-require-full-coverage yes
```
配置解释如下：  
```shell
 1 port 6380                                 #端口号
 2 loglevel notice                           #日志的记录级别，notice是适合生产环境的
 3 logfile "Logs/redis6380_log.txt"          #指定log的保持路径,默认是创建在Redis安装目录下，如果有子目录需要手动创建，如此处的Logs目录
 4 syslog-enabled yes                        #是否使用系统日志
 5 syslog-ident redis6380                    #在系统日志的标识名
 6 appendonly yes                            #数据的保存为aof格式
 7 appendfilename "appendonly.6380.aof"      #数据保存文件
 8 cluster-enabled yes                       #是否开启集群
 9 cluster-config-file nodes.6380.conf
10 cluster-node-timeout 15000
11 cluster-slave-validity-factor 10
12 cluster-migration-barrier 1
13 cluster-require-full-coverage yes
```
5.启动上面6个redis服务  
```shell
redis-server.exe redis.windows-service-6380.conf
redis-server.exe redis.windows-service-6381.conf
redis-server.exe redis.windows-service-6382.conf
redis-server.exe redis.windows-service-6383.conf
redis-server.exe redis.windows-service-6384.conf
redis-server.exe redis.windows-service-6385.conf
```
启动日志分别如下：   
6380：  
```shell
[112188] 07 Jan 01:22:19.058 # Server started, Redis version 3.2.100
[112188] 07 Jan 01:22:19.059 * The server is now ready to accept connections on port 6380
[120928] 07 Jan 01:26:25.306 * Node configuration loaded, I'm 24655b682d670fbe41c4af744d1f4aabb9faf543
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in cluster mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6380
 |    `-._   `._    /     _.-'    |     PID: 120928
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

[120928] 07 Jan 01:26:25.307 # Server started, Redis version 3.2.100
[120928] 07 Jan 01:26:25.307 * The server is now ready to accept connections on port 6380
```
6381:  
```shell
[113380] 07 Jan 01:27:45.982 * No cluster configuration found, I'm eb897702e2019e3a1f4f7487aff6e2d59bb8606f
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in cluster mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6381
 |    `-._   `._    /     _.-'    |     PID: 113380
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

[113380] 07 Jan 01:27:45.994 # Server started, Redis version 3.2.100
[113380] 07 Jan 01:27:45.994 * The server is now ready to accept connections on port 6381
```
6382:  
```shell
[126232] 07 Jan 01:28:14.428 * No cluster configuration found, I'm cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in cluster mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6382
 |    `-._   `._    /     _.-'    |     PID: 126232
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

[126232] 07 Jan 01:28:14.443 # Server started, Redis version 3.2.100
[126232] 07 Jan 01:28:14.443 * The server is now ready to accept connections on port 6382
```
6383:  
```shell
[111404] 07 Jan 01:28:31.819 * No cluster configuration found, I'm b6a468e9bfb0cca917bb65b87ac9551a400d06bd
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in cluster mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6383
 |    `-._   `._    /     _.-'    |     PID: 111404
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

[111404] 07 Jan 01:28:31.891 # Server started, Redis version 3.2.100
[111404] 07 Jan 01:28:31.892 * The server is now ready to accept connections on port 6383
```
6384：  
```shell
[124496] 07 Jan 01:28:45.395 * No cluster configuration found, I'm be3cf9a18f449020449b259a7b00dd9a281ce4e0
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in cluster mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6384
 |    `-._   `._    /     _.-'    |     PID: 124496
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

[124496] 07 Jan 01:28:45.407 # Server started, Redis version 3.2.100
[124496] 07 Jan 01:28:45.407 * The server is now ready to accept connections on port 6384
```
6385:  
```shell
[118964] 07 Jan 01:28:58.883 * No cluster configuration found, I'm 95c6701875e8c1ca8551ce5e6b8665b6c2ce26cf
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.100 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in cluster mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6385
 |    `-._   `._    /     _.-'    |     PID: 118964
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

[118964] 07 Jan 01:28:58.895 # Server started, Redis version 3.2.100
[118964] 07 Jan 01:28:58.896 * The server is now ready to accept connections on port 6385
```
6.执行创建集群命令  
```shell
redis-trib.rb create --replicas 1 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 127.0.0.1:6385
```
结果如下：  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-trib.rb create --replicas 1 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1
:6383 127.0.0.1:6384 127.0.0.1:6385
>>> Creating cluster
Connecting to node 127.0.0.1:6380: OK
Connecting to node 127.0.0.1:6381: OK
Connecting to node 127.0.0.1:6382: OK
Connecting to node 127.0.0.1:6383: OK
Connecting to node 127.0.0.1:6384: OK
Connecting to node 127.0.0.1:6385: OK
>>> Performing hash slots allocation on 6 nodes...
Using 3 masters:
127.0.0.1:6380
127.0.0.1:6381
127.0.0.1:6382
Adding replica 127.0.0.1:6383 to 127.0.0.1:6380
Adding replica 127.0.0.1:6384 to 127.0.0.1:6381
Adding replica 127.0.0.1:6385 to 127.0.0.1:6382
M: 24655b682d670fbe41c4af744d1f4aabb9faf543 127.0.0.1:6380
   slots:0-5460 (5461 slots) master
M: eb897702e2019e3a1f4f7487aff6e2d59bb8606f 127.0.0.1:6381
   slots:5461-10922 (5462 slots) master
M: cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383 127.0.0.1:6382
   slots:10923-16383 (5461 slots) master
S: b6a468e9bfb0cca917bb65b87ac9551a400d06bd 127.0.0.1:6383
   replicates 24655b682d670fbe41c4af744d1f4aabb9faf543
S: be3cf9a18f449020449b259a7b00dd9a281ce4e0 127.0.0.1:6384
   replicates eb897702e2019e3a1f4f7487aff6e2d59bb8606f
S: 95c6701875e8c1ca8551ce5e6b8665b6c2ce26cf 127.0.0.1:6385
   replicates cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join.....
>>> Performing Cluster Check (using node 127.0.0.1:6380)
M: 24655b682d670fbe41c4af744d1f4aabb9faf543 127.0.0.1:6380
   slots:0-5460 (5461 slots) master
M: eb897702e2019e3a1f4f7487aff6e2d59bb8606f 127.0.0.1:6381
   slots:5461-10922 (5462 slots) master
M: cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383 127.0.0.1:6382
   slots:10923-16383 (5461 slots) master
M: b6a468e9bfb0cca917bb65b87ac9551a400d06bd 127.0.0.1:6383
   slots: (0 slots) master
   replicates 24655b682d670fbe41c4af744d1f4aabb9faf543
M: be3cf9a18f449020449b259a7b00dd9a281ce4e0 127.0.0.1:6384
   slots: (0 slots) master
   replicates eb897702e2019e3a1f4f7487aff6e2d59bb8606f
M: 95c6701875e8c1ca8551ce5e6b8665b6c2ce26cf 127.0.0.1:6385
   slots: (0 slots) master
   replicates cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
7.查看集群  
```shell
D:\geek\redis\Redis-x64-3.2.100>redis-trib.rb check 127.0.0.1:6380
Connecting to node 127.0.0.1:6380: OK
Connecting to node 127.0.0.1:6382: OK
Connecting to node 127.0.0.1:6384: OK
Connecting to node 127.0.0.1:6385: OK
Connecting to node 127.0.0.1:6381: OK
Connecting to node 127.0.0.1:6383: OK
>>> Performing Cluster Check (using node 127.0.0.1:6380)
M: 24655b682d670fbe41c4af744d1f4aabb9faf543 127.0.0.1:6380
   slots:0-5460 (5461 slots) master
   1 additional replica(s)
M: cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383 127.0.0.1:6382
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
S: be3cf9a18f449020449b259a7b00dd9a281ce4e0 127.0.0.1:6384
   slots: (0 slots) slave
   replicates eb897702e2019e3a1f4f7487aff6e2d59bb8606f
S: 95c6701875e8c1ca8551ce5e6b8665b6c2ce26cf 127.0.0.1:6385
   slots: (0 slots) slave
   replicates cf86e2ad1d0f4c1e72027b4f90ec075b0bb27383
M: eb897702e2019e3a1f4f7487aff6e2d59bb8606f 127.0.0.1:6381
   slots:5461-10922 (5462 slots) master
   1 additional replica(s)
S: b6a468e9bfb0cca917bb65b87ac9551a400d06bd 127.0.0.1:6383
   slots: (0 slots) slave
   replicates 24655b682d670fbe41c4af744d1f4aabb9faf543
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```







