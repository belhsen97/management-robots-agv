FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Driveless Service that orchestrates and guards robot exchanges."
EXPOSE 52735
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/driveless.api-1.0-SNAPSHOT.jar /app/driveless-service.jar
ENTRYPOINT ["java", "-jar","driveless-service.jar"]