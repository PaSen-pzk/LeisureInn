# LeisureInn
摸鱼小馆
  
## 环境部署  
### 1. Redis (6379)
`/usr/local/bin/redis-server /data/packages/redis7/redis-7.2.4/myredis/redis7.conf`
> ①启动redis
`systemctl start redis7.service`  
> ②查询redis状态
> `systemctl status redis7.service`    
> ③start.sh 脚本启动  
给start.sh文件授权：chmod 744 start.sh   
启动服务，在当前目录下执行：./start.sh start  
关闭服务，在当前目录下执行：./start.sh stop  
重启服务，在当前目录下执行：./start.sh restart  
查看服务状态，在当前目录下执行：./start.sh status  

### 2. xxl-job-admin (9100)

#### 2.1  xxl-job-executor (9110)

### 3. sli应用 (9000)  


### 4. Mysql (3306) 


### 5. rabbit (5672)












