FROM openjdk:8-alpine

ADD target/*.jar target.jar

EXPOSE 8102

#ENTRYPOINT ["java", "-jar", "/target.jar"]
ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=assembly-prod,plugin,prod"]