FROM openjdk:11
EXPOSE 8088
WORKDIR /app
#COPY ./src/main/resources/user /app/user
COPY ./src/main/resources /app
# Copy your Spring Boot JAR file to the container
COPY target/driveless.api-0.0.1-SNAPSHOT.jar /app/driveless-api.jar

#ADD /target/web.api-0.0.1-SNAPSHOT.jar driveless-api.jar

ENTRYPOINT ["java", "-jar","driveless-api.jar"]