FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Notification Service that manages the sending of notifications to users if the mail notification redirects them to mail service."
EXPOSE 52737
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/notification.api-1.0-SNAPSHOT.jar /app/notification-service.jar
ENTRYPOINT ["java", "-jar","notification-service.jar"]