# alpine环境安装gdal 原文连接 https://blog.csdn.net/this_is_id/article/details/90746668?fileGuid=DjvpjdhCprQg9GkP
FROM alpine:3.9

MAINTAINER Mxy

RUN echo "http://mirrors.aliyun.com/alpine/v3.9/main/" > /etc/apk/repositories && \
    echo "https://mirrors.aliyun.com/alpine/edge/testing/" >> /etc/apk/repositories && \
    \
    apk add --no-cache gcc g++ python3 python3-dev linux-headers libc-dev && \
    apk add --no-cache bash bash-doc bash-completion && \
    apk add --no-cache geos geos-dev proj4 proj4-dev && \
    apk add --no-cache gdal gdal-dev && \
    \
    pip3 install supervisor gdal==2.4.0 -i http://pypi.douban.com/simple --trusted-host pypi.douban.com && \
    \
    echo_supervisord_conf > /etc/supervisord.conf && \
    echo "[include]" >> /etc/supervisord.conf && \
    echo "files = /etc/supervisord.d/*.conf" >> /etc/supervisord.conf && \
    \
    rm -rf /var/cache/apk/* && \
    rm -rf ~/.cache/pip/*

ENTRYPOINT ["supervisord", "-n","-c", "/etc/supervisord.conf"]