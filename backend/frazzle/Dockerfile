FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/*.jar application.jar
 
ENTRYPOINT ["java", 
"-Dspring.config.location=/secret/application-develop.properties",
"-jar","/application.jar"]