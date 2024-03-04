FROM limaofeng/java:8

ARG version_number=0

ADD /app.jar /tmp/app/app.jar

# 构建应用
RUN cd /tmp/app && \
    # 拷贝编译结果到指定目录
    mv /tmp/app/app.jar /app/app.jar && \
    #清理编译痕迹
    rm -rf /tmp/app

# 设置编码格式
ENV LANG="zh_CN.UTF-8"

VOLUME ["/logs"]

EXPOSE 8080

ENV VERSION_NUMBER=$version_number

ENTRYPOINT java -server -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -Xmx2G -Xms512m -Xmn600M -XX:PermSize=50M -XX:MaxPermSize=50M -Xss256K -XX:+DisableExplicitGC -XX:SurvivorRatio=1 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -XX:LargePageSizeInBytes=128M -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80 -XX:SoftRefLRUPolicyMSPerMB=0 -Xnoagent -Dspring.profiles.active=prod -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar