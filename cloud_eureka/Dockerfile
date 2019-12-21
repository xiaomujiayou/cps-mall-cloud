FROM openjdk:8-alpine

ADD target/*.jar target.jar

EXPOSE 8101

#ENTRYPOINT ["java", "-jar", "/target.jar"]
ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=prod"]