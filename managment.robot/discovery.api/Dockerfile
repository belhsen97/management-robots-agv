FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Configure Service that allows services to find and communicate with each other."
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/discovery.api-1.0-SNAPSHOT.jar /app/discovery-service.jar
ENTRYPOINT ["java", "-jar","discovery-service.jar"]
