# Docker笔记

## Docker常用命令

### 帮助命令

```shell
docker COMMAND --help # 查看所有帮助
```



### 镜像命令

docker images  查看所有本地主机的镜像

~~~shell
REPOSITORY                   TAG       IMAGE ID       CREATED        SIZE
linuxserver/libreoffice      <none>    e137c22a1823   2 weeks ago    1.2GB

#解释
REPOSITORY 镜像的仓库源
TAG				 镜像的标签
IMAGE ID   镜像ID
CREATED    创建时间
SIZE			 镜像大小

# 参数说明
-a -all    			#列出所有镜像
-q --quiet 			# 只显示镜像ID
~~~

docker search 搜索镜像

```shell
[root@iZ8vb3dwh9qaj9jt1nkz36Z ~]# docker search mysql
NAME                              DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
mysql                             MySQL is a widely used, open-source relation…   11354     [OK]
mariadb                           MariaDB Server is a high performing open sou…   4311      [OK]
mysql/mysql-server                Optimized MySQL Server Docker images. Create…   844                  [OK]

# 参数说明
--filter=stars=3000 # 搜索stars数大于3000的
```

docker pull 下载镜像

```shell
docker pull 镜像名[:tag] #如果不写tag，默认就是latest
```

docker rmi 删除镜像

```shell
docker rmi 镜像ID 							# 删除镜像
docker rmi -f 镜像ID 						# 强制删除镜像
docker rmi -f 镜像ID 	镜像ID 镜像ID 					# 强制删除多个镜像
docker rmi -f ${docker images -aq}   # 强制删除全部的容器
```

### 容器命令

```shell
docker run [可选参数] 镜像ID
# 参数说明
--name="Name" 							# 容器名称（自定义，用于区分容器）tomcat01 tomcat02
-d													# 后台方式运行
-it 												# 使用交互方式运行，进入容器内部查看内容
-p													# 指定容器的端口 -p 8080:8080（即将容器8080端口映射到主机8080端口）
	-p ip:主机端口:容器端口
	-p 主机端口:容器端口 （常用）
	-p 容器端口
	容器端口
-P 													# 随机指定容器端口

docker run -it 容器名称/id /bin/bash      # 启动并进入容器内部
exit																  	# 从容器中退回主机
```

列出所有运行的容器

~~~shell
docker ps 命令
# 参数说明
-a 						# 列出所有容器，包含正在运行的容器以及历史容器
-n=? 					# 显示最近创建的容器
-q 						# 只显示容器id
~~~

退出容器

~~~shell
exit  				# 直接退出容器
Ctrl + P + Q  # 容器不停止退出
~~~

删除容器

~~~shell
docker rm 容器ID
docker rm -f ${docker ps -aq} 		# 删除所有的容器
docker ps -a -q|xargs docker rm   # 删除所有的容器
~~~

启动和停止容器

~~~shell
docker start 容器id
docker restart 容器id
docker stop 容器id
docker kill 容器id    # 强制停止当前容器
~~~





