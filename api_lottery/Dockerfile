FROM openjdk:8-alpine

ADD target/*.jar target.jar

EXPOSE 8111

ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=prod,plugin,cloud-prod,assembly-prod,api-prod,seata-prod"]