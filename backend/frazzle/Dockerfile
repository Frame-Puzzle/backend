FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/*.jar application.jar

EXPOSE 8080
 
ENTRYPOINT ["java","-jar","/app/application.jar"]