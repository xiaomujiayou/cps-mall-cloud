FROM openjdk:8-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig

ADD target/*.jar target.jar

EXPOSE 8109

ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=prod,plugin,cloud-prod,assembly-prod,api-prod,seata-prod","-Duser.timezone=GMT+08"]