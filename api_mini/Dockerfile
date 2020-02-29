FROM openjdk:8-alpine

RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig

ADD target/*.jar target.jar

EXPOSE 8110

ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=assembly-prod,plugin,prod"]