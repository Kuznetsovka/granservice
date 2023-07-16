FROM openjdk:latest
ARG JAR_FILE=target/granservice-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} gran-docs.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","gran-docs.jar"]