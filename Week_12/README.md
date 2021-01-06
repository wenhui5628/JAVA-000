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
redis-server.exe redis.windows6381.con
```
5.查看主从  
redis-cli -h 127.0.0.1 -p 6380 连接6380redis服务  
info Replication 查看主从信息  
```text
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:11056
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
#下面两行注释掉了，是因为低版本redis不支持这两个命令
#sentinel myid fad25e089080be8dddadd3f20e44f888b1f8d48a
#sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 15000
```
sentinel26380.conf文件内容:  
```text
port 26380
#下面两行注释掉了，是因为低版本redis不支持这两个命令
#sentinel myid fad25e089080be8dddadd3f20e44f888b1f8d48b
#sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 15000
```
sentinel26381.conf文件内容:  
```text
port 26381
#下面两行注释掉了，是因为低版本redis不支持这两个命令
#sentinel myid fad25e089080be8dddadd3f20e44f888b1f8d48c
#sentinel deny-scripts-reconfig yes
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
4.关闭6379服务，查看主从变化  
6379没关闭之前，在6380服务看master是6379  
```shell
127.0.0.1:6380> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:down
master_last_io_seconds_ago:-1
slave_repl_offset:36508
master_link_down_since_seconds:jd
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
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:836
slave_priority:100
slave_read_only:1
connected_slaves:0
master_repl_offset:0
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```
这个时候再重新启动6379,6379会自动变成6381的slave  
```shell
D:\install\redis> redis-cli.exe -h 127.0.0.1 -p 6379
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6381
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:20949
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
将下载的"Ruby环境下Redis的驱动文件(redis-3.2.2.gem)"拷贝到Ruby安装根目录下。  
然后执行安装命令如下：  
```shell
gem install --local C:/Ruby27-x64/redis-3.2.2.gem
```
3.将下载的“创建Redis集群的ruby脚本文件redis-trib.rb”文件拷贝到Redis安装根目录(D:\install\redis)下。  
4.新建配置文件redis.windows-service-6380.conf  
```text
port 6380      
loglevel notice    
logfile "D:/install/redis/Logs/redis6380_log.txt"       
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
logfile "D:/install/redis/Logs/redis6381_log.txt"       
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
logfile "D:/install/redis/Logs/redis6382_log.txt"       
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
logfile "D:/install/redis/Logs/redis6383_log.txt"       
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
logfile "D:/install/redis/Logs/redis6384_log.txt"       
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
logfile "D:/install/redis/Logs/redis6385_log.txt"       
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
6.执行创建集群命令  
```shell
redis-trib.rb create --replicas 1 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 127.0.0.1:6385
```
结果如下：  
```shell
D:\install\redis>redis-trib.rb create --replicas 1 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 127.0.0.1:6385
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
M: db4712669500b212790e01e0f3761b30417b1fa9 127.0.0.1:6380
   slots:0-5460 (5461 slots) master
M: af81b3ec8883758a441c277b251477777cf95331 127.0.0.1:6381
   slots:5461-10922 (5462 slots) master
M: 59fd4bf6813665ed781e066a343717d2a0833729 127.0.0.1:6382
   slots:10923-16383 (5461 slots) master
S: bd8addd7b86d6e5d178c66b5441e5ace8b2e67ee 127.0.0.1:6383
   replicates db4712669500b212790e01e0f3761b30417b1fa9
S: 331c145eb0527a93d2e6a4d52b781b039ddf4b0a 127.0.0.1:6384
   replicates af81b3ec8883758a441c277b251477777cf95331
S: a7e8e30b8a70b4eee28523fa56e516edfbeee385 127.0.0.1:6385
   replicates 59fd4bf6813665ed781e066a343717d2a0833729
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join...
>>> Performing Cluster Check (using node 127.0.0.1:6380)
M: db4712669500b212790e01e0f3761b30417b1fa9 127.0.0.1:6380
   slots:0-5460 (5461 slots) master
M: af81b3ec8883758a441c277b251477777cf95331 127.0.0.1:6381
   slots:5461-10922 (5462 slots) master
M: 59fd4bf6813665ed781e066a343717d2a0833729 127.0.0.1:6382
   slots:10923-16383 (5461 slots) master
M: bd8addd7b86d6e5d178c66b5441e5ace8b2e67ee 127.0.0.1:6383
   slots: (0 slots) master
   replicates db4712669500b212790e01e0f3761b30417b1fa9
M: 331c145eb0527a93d2e6a4d52b781b039ddf4b0a 127.0.0.1:6384
   slots: (0 slots) master
   replicates af81b3ec8883758a441c277b251477777cf95331
M: a7e8e30b8a70b4eee28523fa56e516edfbeee385 127.0.0.1:6385
   slots: (0 slots) master
   replicates 59fd4bf6813665ed781e066a343717d2a0833729
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
7.查看集群  
```shell
D:\install\redis>redis-trib.rb check 127.0.0.1:6380
Connecting to node 127.0.0.1:6380: OK
Connecting to node 127.0.0.1:6385: OK
Connecting to node 127.0.0.1:6384: OK
Connecting to node 127.0.0.1:6381: OK
Connecting to node 127.0.0.1:6383: OK
*** WARNING: 127.0.0.1:6385 claims to be slave of unknown node ID 59fd4bf6813665ed781e066a343717d2a0833729.
>>> Performing Cluster Check (using node 127.0.0.1:6380)
M: db4712669500b212790e01e0f3761b30417b1fa9 127.0.0.1:6380
   slots:0-5460 (5461 slots) master
   1 additional replica(s)
S: a7e8e30b8a70b4eee28523fa56e516edfbeee385 127.0.0.1:6385
   slots: (0 slots) slave
   replicates 59fd4bf6813665ed781e066a343717d2a0833729
S: 331c145eb0527a93d2e6a4d52b781b039ddf4b0a 127.0.0.1:6384
   slots: (0 slots) slave
   replicates af81b3ec8883758a441c277b251477777cf95331
M: af81b3ec8883758a441c277b251477777cf95331 127.0.0.1:6381
   slots:5461-10922 (5462 slots) master
   1 additional replica(s)
S: bd8addd7b86d6e5d178c66b5441e5ace8b2e67ee 127.0.0.1:6383
   slots: (0 slots) slave
   replicates db4712669500b212790e01e0f3761b30417b1fa9
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[ERR] Not all 16384 slots are covered by nodes.
```







