#
# Build stage
#
From maven:3.6.0-jdk-13-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:13-jdk-alpine
COPY --from=build /home/app/target/simple-health-checker-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
#EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]