FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Collector Service that collects data from robots to database"
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/collector.api-1.0-SNAPSHOT.jar /app/collector-service.jar
ENTRYPOINT ["java", "-jar","collector-service.jar"]