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

# 可添加参数
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

# 可添加参数
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


```

