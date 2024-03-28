# SEBN-project ( PFE 2024 )

# Note
The project is not yet complete !

# About
Development Web Application to manage, control and monitor AGV robots.

# Description
It is a platform designe to manage the robots in the production line, managing typical elements such as system status, mission status, alerts and notifications, statistics, control mode, fleet management, etc., as well as the importance of real time as robots move simultaneously through the line on both the interaction and control sides, in order to guarantee maintenance time, and add or remove robots in the event of failure.
In short, an AGV robot dashboard is designed to provide users with complete visibility of the AGV robot's operation and performance, as well as the tools to monitor it and react quickly if necessary.


## Built With

* [![Angular][Angular.io]][Angular-url]
* [![ngrx][ngrx.io]][ngrx-url]
* [![Bootstrap][Bootstrap.com]][Bootstrap-url]
* [![material-UI][material-UI.io]][material-UI-url]
* [![Java][Java.io]][Java-url]
* [![Spring Boot][Spring-Boot.io]][Spring-Boot-url]
* [![mongodb][mongodb.com]][mongodb-url]
* [![docker][docker.com]][docker-url]

## Installation
### Front
Use Angular CLI [ng](https://angular.io/cli/version) to add external libraries.

```bash
ng add @ngrx/store@16.2.0
ng add @ngrx/store-devtools@16.2.0
ng add @ngrx/router-store@16.2.0
ng add @ngrx/effects@16.2.0
ng add @angular/material
ng add highcharts@11.4
```
### Service Web API 
Add in pom.xml this dependencies below.
```xml
	<properties>
		<maven.compiler.source>11</maven.compiler.source>
		<maven.compiler.target>11</maven.compiler.target>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!--  json web token  -->
		<jjwt.version>0.11.5</jjwt.version>
		<!-- javax mail -->
		<javax.mail.version>1.6.2</javax.mail.version>
		<!-- jsoup -->
		<jsoup.version>1.17.2</jsoup.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-mongodb</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<!-- jsoup HTML parser library @ https://jsoup.org/ -->
			<groupId>org.jsoup</groupId>
			<artifactId>jsoup</artifactId>
			<version>${jsoup.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
		<dependency>
			<groupId>javax.mail</groupId>
			<artifactId>javax.mail-api</artifactId>
			<version>${javax.mail.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>${jjwt.version}</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>${jjwt.version}</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>${jjwt.version}</version>
		</dependency>
	</dependencies>
```
### Collector API
Add in pom.xml this dependencies below.
```xml
	<properties>
		<java.version>11</java.version>
		<maven.compiler.source>11</maven.compiler.source>
		<maven.compiler.target>11</maven.compiler.target>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<!--  jackson  -->
		<jackson.version>2.13.4</jackson.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.integration</groupId>
			<artifactId>spring-integration-mqtt</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-mongodb</artifactId>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
			<version>${jackson.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-aop</artifactId>
		</dependency>
	</dependencies>
```



## Running ( on windows )


### Database
> [!NOTE]
> If you have OS windows and MongoDB installed use this command line.
1. Run service MongoDB Server (MongoDB) in bash.
   ```bash
    NET START MongoDB
   ```
### Server API Spring

> [!NOTE]
> If you dont have development environment and you have mvn use this steps

1. Make file .jar for java project 
   ```bash
   mvn clean
   mvn install
   ```
2. Then you will see in web.api\target\web.api-0.0.1-SNAPSHOT.jar run it.
   ```bash
   java -jar    -noverify  web.api\target\web.api-0.0.1-SNAPSHOT.jar
   ```
   
> [!NOTE]
> You will find file run-web-api.bat

### Front Angular
1. first add node_Modules and in \front : 
   ```bash
   ng serve
   ```


## Running ( on docker )
 Install docker on your machine then press in terminal below:
   ```bash
   .../SEBN-project>docker-compose up -d
   [+] Running 4/4
   service in your compose file, you can run this command with the --remove-orphans flag to clean it up."
   [+] Running 4/4
    ✔ Container mqttx-web      Started  2.5s
    ✔ Container emqx           Started  3.7s
    ✔ Container mongodb        Started  2.1s
    ✔ Container collector-api  Started  5.1s
    ...
   ```
 then you will see all sevices are running:
   ```bash
.../SEBN-project>docker-compose ps
NAME                IMAGE                 COMMAND                  SERVICE             CREATED             STATUS              PORTS
collector-api       collector-api:0.0.1   "java -jar collector…"   collector-api       2 hours ago         Up 2 hours          0.0.0.0:8090->8090/tcp
emqx                emqx/emqx:latest      "/usr/bin/docker-ent…"   emqx                2 hours ago         Up 2 hours          4370/tcp, 0.0.0.0:1883->1883/tcp, 0.0.0.0:8083-8084->8083-8084/tcp, 0.0.0.0:8883->8883/tcp, 0.0.0.0:18083->18083/tcp, 5369/tcp
mongodb             mongo:latest          "docker-entrypoint.s…"   mongodb             2 hours ago         Up 2 hours          0.0.0.0:27017->27017/tcp
mqttx-web           emqx/mqttx-web        "docker-entrypoint.s…"   mqttx-web           2 hours ago         Up 2 hours          0.0.0.0:18084->80/tcp
    ...
   ```
   
 to stop all services press:
   ```bash
.../SEBN-project> docker-compose down
[+] Running 5/5
 ✔ Container collector-api              Removed    1.4s
 ✔ Container mqttx-web                  Removed    1.2s
 ✔ Container mongodb                    Removed    0.8s
 ✔ Container emqx                       Removed    3.5s
 ✔ Network sebn-project_ennova-network  Removed    0.9s
    ...
   ```



<!--https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax-->

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[Angular.io]: https://img.shields.io/badge/Angular%20v16.2.0-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[ngrx.io]: https://img.shields.io/badge/ngrx%20v16.2.0-a829c3?style=for-the-badge&logo=ngrx&logoColor=white
[ngrx-url]: https://ngrrex.io/
[material-UI.io]: https://img.shields.io/badge/material%20UI%20v16.2.14-007FFF?style=for-the-badge&logo=mui&logoColor=white
[material-UI-url]: https://material.angular.io/
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap%20v4.4.1-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[Java.io]: https://img.shields.io/badge/Java%20v11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/fr/
[Spring-Boot.io]: https://img.shields.io/badge/Spring%20v2.6.9-97CA00?style=for-the-badge&logo=spring&logoColor=white
[Spring-Boot-url]: https://spring.io/projects/spring-boot
[mongodb.com]: https://img.shields.io/badge/Mongodb%20v6.0-4DA53F?style=for-the-badge&logo=mongodb&logoColor=white
[mongodb-url]: https://www.mongodb.com/
[docker.com]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white
[docker-url]: https://www.docker.com/