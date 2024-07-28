FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/*.jar application.jar

EXPOSE 8080
 
ENTRYPOINT ["java","-Dspring.config.location=/secret/application-develop.properties","-jar","/app/application.jar"]