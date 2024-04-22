##### # docker部署nginx（linux）
~~~
 docker run -id --name=c_tomcat \
 -p 8080:8080 \
 -v $PWD:/usr/local/tomcat/webapps \
 tomcat
 
 docker run -itd \ 
--name nginx02 \
-v /home/nginx/conf.d:/etc/nginx/conf.d \ (以下是将/home/nginx的内容挂载到对应的目录下，注意位置千万不能搞错)
-v /home/nginx/html:/etc/nginx/html \
-v /home/nginx/log:/usr/log/nginx \
-p 8081:80 \
nginx
~~~
> -p 8080:8080： 将容器的8080端⼝映射到主机的8080端⼝
> -v $PWD:/usr/local/tomcat/webapps： 将主机中当前⽬录挂载到容器的webapps
> 原文链接：https://blog.csdn.net/qq_58804301/article/details/123699684

~~~
mkdir -p /home/dockerdata/nginx/{log,conf,html}
docker run --name nginx -d nginx
docker cp nginx:/etc/nginx/nginx.conf /home/dockerdata/nginx/conf/
docker cp nginx:/etc/nginx/conf.d/default.conf /home/dockerdata/nginx/conf

docker run --privileged --name nginx -it -p 8081:80 -v /home/dockerdata/nginx/conf/nginx.conf:/etc/nginx/nginx.conf:ro -v /home/dockerdata/nginx/conf/default.conf:/etc/nginx/conf.d/default.conf:ro -v /home/dockerdata/nginx/html:/usr/share/nginx/html:rw -v /home/dockerdata/nginx/log:/var/log/nginx -d nginx
~~~
>--privileged
>
> 使用该参数，[container]内的root拥有真正的root权限。
> 否则，container内的root只是外部的一个普通用户权限。
> privileged启动的容器，可以看到很多host上的设备，并且可以执行mount。
> 甚至允许你在docker容器中启动docker容器。
>
>--name
>
> 设置nginx容器的名称
>
>-p 8193:8193
>
> 设置访问端口和nginx容器的监听端口的映射关系
>
> 第一个8193是你访问的端口
>
> 第二个8193是docker的nginx配置文件监听端口
>
>-d 后台挂载运行nginx
>
>-v /home/dockers/nginx/conf/nginx.conf:/etc/nginx/nginx.conf:ro
>-v /home/dockers/nginx/conf/default.conf:/etc/nginx/conf.d/default.conf:ro
>-v /home/dockers/nginx/html:/usr/share/nginx/html:rw
>-v /home/dockers/nginx/log:/var/log/nginx
> 
> 原文链接： https://www.cnblogs.com/jouncy/p/16166122.html

##### docker部署git(win)

docker run -d  --hostname localhost  -p 10080:80  -p 10443:443  --name gitlab  --restart unless-stopped  -v "E:\gitlab/etc":/etc/gitlab  -v "E:\gitlab/log":/var/log/gitlab  -v "E:\gitlab/data":/var/data/gitlab  gitlab/gitlab-ce:latest

##### docker部署git(linux)

docker run -d  --hostname localhost  -p 10080:80  -p 10443:443  --name gitlab  --restart unless-stopped  -v "/home/gitlab/etc":/etc/gitlab  -v "/home/gitlab/log":/var/log/gitlab  -v "/home/gitlab/data":/var/data/gitlab  gitlab/gitlab-ce:latest

##### docker部署mongo（linux）
~~~
docker run \
-d \
--name mongo \
--restart=always \
--privileged=true \
-p 27017:27017 \
-v /home//mongodb/data:/data/db \
mongo:4.2 --auth

–auth：需要密码才能访问容器服务。
~~~
参考链接：
> https://cloud.tencent.com/developer/article/2347965
> 
> https://blog.csdn.net/liuyunshengsir/article/details/127924865

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



##### docker部署gitGogs

~~~docker
docker run -d --name=gogs -p 10022:22 -p 10080:3000 -v /var/gogs:/data gogs/gogs
~~~

> 原文： https://blog.51cto.com/u_14671216/6397322



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

```sh
docker run -d --name redis -p6379:6379 -v /d/redis/data:/data redis --appendonly yes
```

> **参数说明：**
> **-d** ------ 后台运行
> **--name** ------ 实例运行后的名字 myredis
> **-p6379:6379** ------ 端口映射，冒号前面是windows下的端口，后面是虚拟机的端口
> **-v /d/redis/data:/data** ------ 保存数据的位置。
>
> - d:\redis\data 前面是windows下的实际保存数据目录
> - /data 虚拟机内的目录
>
> **redis-server --appendonly yes** ------ 在容器执行redis-server启动命令，并打开redis持久化配置。

> 原文链接：https://www.moguf.com/post/windockerrunredis

##### Docker 安装kkFile

docker run -it -p 8012:8012 keking/kkfileview

##### docker 部署mysql

第一种

```sh
docker run --name mysql --restart=always -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql
```

第二种

```sh
docker run --restart=always --privileged=true -d -v /home/mysql/data:/var/lib/mysql -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/my.cnf:/etc/mysql/my.cnf -p 3311:3306 --name mysql02 -e MYSQL_ROOT_PASSWORD=root mysql
```

- `--restart=always` 代表开启启动
- `--privileged=true` 代表进入容器内部为管理员身份
- `-d` 表示后台运行容器 并返回容器Id
- `-v` 把mysql产生的数据同步到本地 防止数据丢失
- `-e` 容器传参  设置mysql的初始密码

##### Docker安装ElasticSearch 版本7.6.2

```dockerfile
docker run -p 9200:9200 -p 9300:9300 --privileged=true --name es7.6.2 -e "discovery.type=single-node"  -v D:\elasticsearch\plugins:/usr/share/elasticsearch/plugins -v D:\elasticsearch\config:/usr/share/elasticsearch/config -v D:\elasticsearch\data:/usr/share/elasticsearch/data -v D:\elasticsearch\logs:/usr/share/elasticsearch/logs -d elasticsearch:7.6.2
```

> 原文链接：https://blog.csdn.net/yexiaomodemo/article/details/112966842

##### docker安装kibana-7.6.1

```dockerfile
docker run -itd --name kibana -p 5601:5601 -e "ELASTICSEARCH_HOSTS=http://localhost:9200" docker.elastic.co/kibana/kibana:7.6.2
```

> 原文链接：https://blog.csdn.net/weixin_42854904/article/details/118756989

##### docker安装文件共享

```
docker run -d --restart=always -e FILE_SIZE_LIMIT=1000 -e ERROR_COUNT=15 -e ERROR_MINUTE=1  -p 9093:12345 -v /opt/FileCodeBox/:/app/data --name filecodebox lanol/filecodebox:latest
```

> 原文链接：https://blog.csdn.net/weixin_42854904/article/details/118756989

##### docker安装nps内网穿透

```
docker run -d --name npc --net=host ffdfgdfg/npc -server=39.103.151.150:8024 -vkey=test
```
> 原文链接：https://blog.csdn.net/m0_64349423/article/details/130339223

##### docker-compose安装photoprism

```dockerfile
version: '3.5'

# Example Docker Compose config file for PhotoPrism (Windows / AMD64)
#
# Note:
# - Running PhotoPrism on a server with less than 4 GB of swap space or setting a memory/swap limit can cause unexpected
#   restarts ("crashes"), for example, when the indexer temporarily needs more memory to process large files.
# - Windows Pro users should disable the WSL 2 based engine in Docker Settings > General so that
#   they can mount drives other than C:. This will enable Hyper-V, which Microsoft doesn't offer
#   to its Windows Home customers. Docker Desktop uses dynamic memory allocation with WSL 2.
#   It's important to explicitly increase the Docker memory limit to 4 GB or more when using Hyper-V.
#   The default of 2 GB may reduce indexing performance and cause unexpected restarts.
# - If you install PhotoPrism on a public server outside your home network, please always run it behind a secure
#   HTTPS reverse proxy such as Traefik or Caddy. Your files and passwords will otherwise be transmitted
#   in clear text and can be intercepted by anyone, including your provider, hackers, and governments:
#   https://docs.photoprism.app/getting-started/proxies/traefik/
#
# Setup Guide:
# - https://docs.photoprism.app/getting-started/docker-compose/
# - https://www.photoprism.app/kb/activation
#
# Troubleshooting Checklists:
# - https://docs.photoprism.app/getting-started/troubleshooting/
# - https://docs.photoprism.app/getting-started/troubleshooting/docker/
# - https://docs.photoprism.app/getting-started/troubleshooting/mariadb/
# - https://docs.photoprism.app/getting-started/troubleshooting/windows/
#
# CLI Commands:
# - https://docs.photoprism.app/getting-started/docker-compose/#command-line-interface

services:
  photoprism:
    ## Use photoprism/photoprism:preview for testing preview builds:
    image: photoprism/photoprism:latest
    ## Don't enable automatic restarts until PhotoPrism has been properly configured and tested!
    ## If the service gets stuck in a restart loop, this points to a memory, filesystem, network, or database issue:
    ## https://docs.photoprism.app/getting-started/troubleshooting/#fatal-server-errors
    # restart: unless-stopped
    stop_grace_period: 10s
    depends_on:
      - mariadb
    security_opt:
      - seccomp:unconfined
      - apparmor:unconfined
    ports:
      - "2342:2342" # HTTP port (host:container)
    environment:
      PHOTOPRISM_ADMIN_USER: "admin"                 # admin login username
      PHOTOPRISM_ADMIN_PASSWORD: "myz123456"          # initial admin password (8-72 characters)
      PHOTOPRISM_AUTH_MODE: "password"               # authentication mode (public, password)
      PHOTOPRISM_SITE_URL: "http://localhost:2342/"  # server URL in the format "http(s)://domain.name(:port)/(path)"
      PHOTOPRISM_DISABLE_TLS: "false"                # disables HTTPS/TLS even if the site URL starts with https:// and a certificate is available
      PHOTOPRISM_DEFAULT_TLS: "true"                 # defaults to a self-signed HTTPS/TLS certificate if no other certificate is available
      PHOTOPRISM_ORIGINALS_LIMIT: 5000               # file size limit for originals in MB (increase for high-res video)
      PHOTOPRISM_HTTP_COMPRESSION: "gzip"            # improves transfer speed and bandwidth utilization (none or gzip)
      PHOTOPRISM_DEBUG: "false"                      # run in debug mode, shows additional log messages
      PHOTOPRISM_READONLY: "false"                   # do not modify originals folder; disables import, upload, and delete
      PHOTOPRISM_EXPERIMENTAL: "false"               # enables experimental features
      PHOTOPRISM_DISABLE_CHOWN: "false"              # disables updating storage permissions via chmod and chown on startup
      PHOTOPRISM_DISABLE_WEBDAV: "false"             # disables built-in WebDAV server
      PHOTOPRISM_DISABLE_SETTINGS: "false"           # disables settings UI and API
      PHOTOPRISM_DISABLE_TENSORFLOW: "false"         # disables all features depending on TensorFlow
      PHOTOPRISM_DISABLE_FACES: "false"              # disables face detection and recognition (requires TensorFlow)
      PHOTOPRISM_DISABLE_CLASSIFICATION: "false"     # disables image classification (requires TensorFlow)
      PHOTOPRISM_DISABLE_VECTORS: "false"            # disables vector graphics support
      PHOTOPRISM_DISABLE_RAW: "false"                # disables indexing and conversion of RAW images
      PHOTOPRISM_RAW_PRESETS: "false"                # enables applying user presets when converting RAW images (reduces performance)
      PHOTOPRISM_JPEG_QUALITY: 85                    # a higher value increases the quality and file size of JPEG images and thumbnails (25-100)
      PHOTOPRISM_DETECT_NSFW: "false"                # automatically flags photos as private that MAY be offensive (requires TensorFlow)
      PHOTOPRISM_UPLOAD_NSFW: "true"                 # allows uploads that MAY be offensive (no effect without TensorFlow)
      PHOTOPRISM_DATABASE_DRIVER: "mysql"            # use MariaDB 10.5+ or MySQL 8+ instead of SQLite for improved performance
      PHOTOPRISM_DATABASE_SERVER: "mariadb:3306"     # MariaDB or MySQL database server hostname (:port is optional)
      PHOTOPRISM_DATABASE_NAME: "photoprism"         # MariaDB or MySQL database schema name
      PHOTOPRISM_DATABASE_USER: "photoprism"         # MariaDB or MySQL database user name
      PHOTOPRISM_DATABASE_PASSWORD: "insecure"       # MariaDB or MySQL database user password
      PHOTOPRISM_SITE_CAPTION: "AI-Powered Photos App"
      PHOTOPRISM_SITE_DESCRIPTION: ""                # meta site description
      PHOTOPRISM_SITE_AUTHOR: ""                     # meta site author
      ## Video Transcoding (https://docs.photoprism.app/getting-started/advanced/transcoding/):
      # PHOTOPRISM_FFMPEG_ENCODER: "software"        # H.264/AVC encoder (software, intel, nvidia, apple, raspberry, or vaapi)
      # PHOTOPRISM_FFMPEG_SIZE: "1920"               # video size limit in pixels (720-7680) (default: 3840)
      # PHOTOPRISM_FFMPEG_BITRATE: "32"              # video bitrate limit in Mbit/s (default: 50)
    working_dir: "/photoprism" # do not change or remove
    ## Storage Folders: use "/" not "\" as separator, "~" is a shortcut for C:/user/{username}, "." for the current directory
    volumes:
      # "C:/user/username/folder:/photoprism/folder"       # example
      - "~/Pictures:/photoprism/originals"                 # original media files (photos and videos)
      # - "D:/example/family:/photoprism/originals/family" # *additional* media folders can be mounted like this
      - "H:/Baby/Photos:/photoprism/import"                         # *optional* base folder from which files can be imported to originals
      - "./storage:/photoprism/storage"                    # *writable* storage folder for cache, database, and sidecar files (never remove)

  ## Database Server (recommended)
  ## see https://docs.photoprism.app/getting-started/faq/#should-i-use-sqlite-mariadb-or-mysql
  mariadb:
    image: mariadb
    ## If MariaDB gets stuck in a restart loop, this points to a memory or filesystem issue:
    ## https://docs.photoprism.app/getting-started/troubleshooting/#fatal-server-errors
    restart: unless-stopped
    stop_grace_period: 5s
    security_opt: # see https://github.com/MariaDB/mariadb-docker/issues/434#issuecomment-1136151239
      - seccomp:unconfined
      - apparmor:unconfined
    ## --lower-case-table-names=1 stores tables in lowercase and compares names in a case-insensitive manner
    ## see https://mariadb.com/kb/en/server-system-variables/#lower_case_table_names
    command: mariadbd --innodb-buffer-pool-size=512M --lower-case-table-names=1 --transaction-isolation=READ-COMMITTED --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --max-connections=512 --innodb-rollback-on-timeout=OFF --innodb-lock-wait-timeout=120
    volumes:
      - "database:/var/lib/mysql" # Named volume "database" is defined at the bottom (DO NOT REMOVE)
    environment:
      MARIADB_AUTO_UPGRADE: "1"
      MARIADB_INITDB_SKIP_TZINFO: "1"
      MARIADB_DATABASE: "photoprism"
      MARIADB_USER: "photoprism"
      MARIADB_PASSWORD: "insecure"
      MARIADB_ROOT_PASSWORD: "insecure"

  ## Watchtower upgrades services automatically (optional)
  ## see https://docs.photoprism.app/getting-started/updates/#watchtower
  #
  # watchtower:
  #   restart: unless-stopped
  #   image: containrrr/watchtower
  #   environment:
  #     WATCHTOWER_CLEANUP: "true"
  #     WATCHTOWER_POLL_INTERVAL: 7200 # checks for updates every two hours
  #   volumes:
  #     - "/var/run/docker.sock:/var/run/docker.sock"
  #     - "~/.docker/config.json:/config.json" # optional, for authentication if you have a Docker Hub account

## Create named volumes, advanced users may remove this if they mount a regular host folder
## for the database or use SQLite instead (never remove otherwise)
volumes:
  database:
    driver: local

```

> 原文链接：https://zhuanlan.zhihu.com/p/438779525