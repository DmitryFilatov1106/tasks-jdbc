### 1 variant
FROM openjdk:17-jdk-slim
COPY target/*.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]

### 2 variant
#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /
#COPY /src /src
#COPY checkstyle-suppressions.xml /
#COPY pom.xml /
#RUN mvn -f /pom.xml clean package
#
#FROM openjdk:17-jdk-slim
#COPY --from=build /target/*.jar application.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "application.jar"]