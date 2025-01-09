FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Trackbot Service that manages robots, tags and workstations."
EXPOSE 52738
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/trackbot.api-1.0-SNAPSHOT.jar /app/trackbot-service.jar
ENTRYPOINT ["java", "-jar","trackbot-service.jar"]