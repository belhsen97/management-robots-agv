FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Configure Service that is the central place where all the configuration parameters of the applications are stored and maintained."
EXPOSE 8101
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/configure.api-1.0-SNAPSHOT.jar /app/config-service.jar
ENTRYPOINT ["java", "-jar","config-service.jar"]
