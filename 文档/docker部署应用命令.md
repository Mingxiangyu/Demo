##### # docker部署nginx（linux）

> docker run -id --name=c_tomcat \
  -p 8080:8080 \
  -v $PWD:/usr/local/tomcat/webapps \
  tomcat
> -p 8080:8080： 将容器的8080端⼝映射到主机的8080端⼝
> -v $PWD:/usr/local/tomcat/webapps： 将主机中当前⽬录挂载到容器的webapps

##### docker部署git(win)

docker run -d  --hostname localhost  -p 10080:80  -p 10443:443  --name gitlab  --restart unless-stopped  -v "E:\gitlab/etc":/etc/gitlab  -v "E:\gitlab/log":/var/log/gitlab  -v "E:\gitlab/data":/var/data/gitlab  gitlab/gitlab-ce:latest

##### docker部署git(linux)

docker run -d  --hostname localhost  -p 10080:80  -p 10443:443  --name gitlab  --restart unless-stopped  -v "/home/gitlab/etc":/etc/gitlab  -v "/home/gitlab/log":/var/log/gitlab  -v "/home/gitlab/data":/var/data/gitlab  gitlab/gitlab-ce:latest

##### docker部署mongo（win）

docker volume create --name mongodata #先创建挂载卷，如果指定本机目录（win和os会出现启动时报权限问题）
docker run -p 27017:27017 -v "E:\MongoData":/data/db --name docker_mongodb -d mongo

##### docker部署mongo（win）1

docker volume create --name mongodata #先创建挂载卷，如果指定本机目录（win和os会出现启动时报权限问题）
docker run --name mongodb -v mongodata:/data/db -p 27017:27017 -d mongo:latest --auth
docker exec -it mongodb mongo admin
#进入容器后执行命令创建用户
db.createUser({  user: 'root',  pwd: 'admin',  roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]  });
#授权：返回 1 代表授权验证成功
db.auth("root","admin");

# 创建一个新的数据库 blog_db，并授权
## 创建目标数据库（实际上切换即可）
use blog_db
## 创建目标数据库管理用户
db.createUser({  user: 'blog',  pwd: 'blog123456',  roles: [ { role: "readWrite", db: "test" } ]  });
## 开启验证
db.auth("blog","blog123456");
## MongoDB生成测试数据脚本（直接Navicat查询中执行即可）
for (var i = 1; i <= 9000000; i++) {
   db.testData.insert( { x : i , name: "MACLEAN" , name1:"MACLEAN", name2:"MACLEAN", name3:"MACLEAN"} )
}

##### docker部署gitlab

docker run --detach   --hostname gitlab.example.com   --publish 8929:443 --publish 8980:80 --publish 8922:22   --name gitlab   --restart always   --volume E:labgitlabetc:/etc/gitlab   --volume E:labgitlablog:/var/log/gitlab   --volume E:labgitlabdata:/var/opt/gitlab   gitlab/gitlab-ce:latest

##### docker部署yapi

 docker run -it --rm --link mongo-yapi:mongo --entrypoint npm --workdir /api/vendors registry.cn-hangzhou.aliyuncs.com/anoy/yapi run install-server


 docker run -d --name yapi --link mongo-yapi:mongo --workdir /api/vendors -p 3000:3000 registry.cn-hangzhou.aliyuncs.com/anoy/yapi server/app.js

##### docker部署nexus

docker run -itd -p 8081:8081 --name nexus -e "INSTALL4J_ADD_VM_PARAMS=-Xms128m -Xmx512m -XX:MaxDirectMemorySize=512m -Djava.util.prefs.userRoot=/nexus-data/javaprefs" -e NEXUS_CONTEXT=nexus -v /usr/local/nexus3/nexus-data:/nexus-data  docker.io/sonatype/nexus3

##### docker部署samba

docker run -it -m 512m -p 139:139 -p 445:445 --name samba -d --rm   -v F:\dockersamba:/mount dperson/samba -u "txl;123" -s "txl;/mount/;yes;no;yes;all;all;all" -w "WORKGROUP" -g "force user= txl" -g "guest account= txl" -p

##### Docker部署FastDFS

docker run -d --restart=always --privileged=true --net=host --name=fastdfs -e IP=192.168.127.131 -e WEB_PORT=80 -v ${HOME}/fastdfs:/var/local/fdfs qbanxiaoli/fastdfs
IP 后面是你的服务器公网ip或者虚拟机的IP，-e WEB_PORT=80 指定 nginx 端口

##### Docker 安装运行 Redis

docker run -d -p 6379:6379 redis:4.0.8

##### Docker 安装kkFile

docker run -it -p 8012:8012 keking/kkfileview

##### docker 部署mysql

docker run --name mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql

##### Docker安装ElasticSearch 版本7.6.2

```dockerfile
docker run -p 9200:9200 -p 9300:9300 --privileged=true --name es7.6.2 -e "discovery.type=single-node"  -v D:\elasticsearch\plugins:/usr/share/elasticsearch/plugins -v D:\elasticsearch\config:/usr/share/elasticsearch/config -v D:\elasticsearch\data:/usr/share/elasticsearch/data -v D:\elasticsearch\logs:/usr/share/elasticsearch/logs -d elasticsearch:7.6.2
```

> 原文链接：https://blog.csdn.net/yexiaomodemo/article/details/112966842

##### docker安装kibana-7.6.1

```dockerfile
docker run -itd --name kibana -p 5601:5601 -e \
"ELASTICSEARCH_HOSTS=http://"yourIp":9200" docker.elastic.co/kibana/kibana:7.6.2
```

