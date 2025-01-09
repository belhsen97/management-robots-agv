FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Mail Service that sends emails to users."
EXPOSE 52736
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/mail.api-1.0-SNAPSHOT.jar /app/mail-service.jar
ENTRYPOINT ["java", "-jar","mail-service.jar"]