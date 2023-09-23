#基于那个镜像
FROM openjdk:11-jdk
#将本地文件夹挂载到当前容器
VOLUME /temp
#复制文件到容器
ADD panda-admin-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
#声明需要暴露的端口
EXPOSE 8181
#配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

