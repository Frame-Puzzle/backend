FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/*.jar application.jar
COPY /home/ubuntu/dockerConfig/secret/application-develop.properties /app/secret/application-develop.properties

EXPOSE 8080
 
ENTRYPOINT ["java","-Dspring.config.location=/secret/application-develop.properties","-jar","/app/application.jar"]