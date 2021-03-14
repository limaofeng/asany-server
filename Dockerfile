FROM core.harbor.thuni-h.com/library/java:8

ARG version_number=0

ADD /build/app.jar /tmp/app/app.jar

# 构建应用
RUN cd /tmp/app && \
    mkdir /app && \
    # 拷贝编译结果到指定目录
    mv /tmp/app/app.jar /app/app.jar && \
    #清理编译痕迹
    rm -rf /tmp/app

# 设置编码格式
ENV LANG="zh_CN.UTF-8"

VOLUME ["/logs"]

EXPOSE 8080 9090 50983

ENV VERSION_NUMBER=$version_number

ENTRYPOINT java -server -Dfile.encoding=UTF-8 -Xmx512m -Xss256k -Xdebug -Xnoagent -Djava.compiler=NONE -Dspring.profiles.active=prod -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=50983 -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar