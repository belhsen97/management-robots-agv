FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the User Service that manages users and their permissions."
EXPOSE 52739
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/user.api-1.0-SNAPSHOT.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar","user-service.jar"]