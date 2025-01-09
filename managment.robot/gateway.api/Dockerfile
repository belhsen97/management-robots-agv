FROM openjdk:11
LABEL maintainer="Bachouch Belhsen <belhsenbachouch97@gmail.com>"
LABEL version="1.0"
LABEL description="This is the Gateway Service that manages routing, load balancing and secures access to services using service user."
EXPOSE 8089
WORKDIR /app
COPY ./src/main/resources /app/resources
COPY /target/gateway.api-1.0-SNAPSHOT.jar /app/gateway-service.jar
ENTRYPOINT ["java", "-jar","gateway-service.jar"]
