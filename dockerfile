FROM openjdk:latest
ARG JAR_FILE=target/granservice-1.0-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} gran-docs.jar

ENTRYPOINT ["java","-jar","gran-docs.jar"]