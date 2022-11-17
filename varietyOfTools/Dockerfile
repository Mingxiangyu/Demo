FROM lwieske/java-8

ADD target/*.jar app.jar


# set Time zone
ENV TZ=Asia/Shanghai
RUN set -eux; \
	ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
	echo $TZ > /etc/timezone


ENTRYPOINT ["java","-Dfile.encoding=utf-8","-jar","/app.jar",""]