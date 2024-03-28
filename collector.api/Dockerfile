FROM openjdk:11

WORKDIR /app
#COPY ./src/main/resources/user /app/user
COPY ./src/main/resources /app
# Copy your Spring Boot JAR file to the container
COPY target/collector.api-0.0.1-SNAPSHOT.jar /app/collector-api.jar

#ADD /target/web.api-0.0.1-SNAPSHOT.jar collector-api.jar

ENTRYPOINT ["java", "-jar","collector-api.jar"]