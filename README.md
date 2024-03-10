# SEBN-project ( PFE 2024 )


# About
Development Web Application to manage, control and monitor AGV robots.

# Description
it is a platform designe to manage the robots in the production line, managing typical elements such as system status, mission status, alerts and notifications, statistics, control mode, fleet management, etc., as well as the importance of real time as robots move simultaneously through the line on both the interaction and control sides, in order to guarantee maintenance time, and add or remove robots in the event of failure.
In short, an AGV robot dashboard is designed to provide users with complete visibility of the AGV robot's operation and performance, as well as the tools to monitor it and react quickly if necessary.

# Note
The project is not yet complete !


## Built With

* [![Angular][Angular.io]][Angular-url]
* [![ngrx][ngrx.io]][ngrx-url]
* [![Bootstrap][Bootstrap.com]][Bootstrap-url]
* [![material-UI][material-UI.io]][material-UI-url]
* [![Spring Boot][Spring-Boot.io]][Spring-Boot-url]
* [![mongodb][mongodb.com]][mongodb-url]


## Installation
### Front
Use Angular CLI [ng](https://angular.io/cli/version) to add external libraries.

```bash
ng add @ngrx/store@16.2.0
ng add @ngrx/store-devtools@16.2.0
ng add @ngrx/router-store@16.2.0
ng add @ngrx/effects@16.2.0
ng add @angular/material
ng add highcharts-angular
ng add highcharts 
```
### Back 
Add in pom.xml this dependencies below.
```xml
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
			<version>1.17.2</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
		<dependency>
			<groupId>javax.mail</groupId>
			<artifactId>javax.mail-api</artifactId>
			<version>1.6.2</version>
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
			<version>0.11.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.11.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.11.5</version>
		</dependency>
	</dependencies>
```







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
[Spring-Boot.io]: https://img.shields.io/badge/Spring%20v2.6.9-97CA00?style=for-the-badge&logo=spring&logoColor=white
[Spring-Boot-url]: https://spring.io/projects/spring-boot
[mongodb.com]: https://img.shields.io/badge/Mongodb%20v6.0-4DA53F?style=for-the-badge&logo=mongodb&logoColor=white
[mongodb-url]: https://www.mongodb.com/