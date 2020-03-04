FROM openjdk:8-alpine

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ADD target/*.jar target.jar

EXPOSE 8102

#ENTRYPOINT ["java", "-jar", "/target.jar"]
ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=assembly-prod,plugin,prod","-Duser.timezone=GMT+08"]