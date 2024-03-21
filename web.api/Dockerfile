FROM openjdk:11
EXPOSE 8089
WORKDIR /app
#COPY ./src/main/resources/user /app/user
COPY ./src/main/resources /app
# Copy your Spring Boot JAR file to the container
COPY target/web.api-0.0.1-SNAPSHOT.jar /app/web-api.jar

#COPY ./src/main/resources/user/html/forgot-password.html /app/user/html/forgot-password.html
#COPY ./src/main/resources/user/html/verify-mail-correct.html /app/user/html/verify-mail-correct.html
#COPY ./src/main/resources/user/html/update-new-user.html /app/user/html/update-new-user.html
#COPY ./src/main/resources/user/img/user.png /app/user/img/user.png
#ADD /target/web.api-0.0.1-SNAPSHOT.jar web-api.jar

ENTRYPOINT ["java", "-jar","web-api.jar"]