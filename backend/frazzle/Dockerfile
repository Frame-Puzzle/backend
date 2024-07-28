FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/*.jar application.jar
COPY application-develop.properties /app/src/main/resources/application-develop.properties

EXPOSE 8080
 
ENTRYPOINT ["java","-Dspring.config.location=/app/src/main/resources/application-develop.properties","-jar","/app/application.jar"]