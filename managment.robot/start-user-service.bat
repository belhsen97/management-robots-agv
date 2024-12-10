echo Start User
title User-Service
color 70
mode con: cols=100 lines=25
java -jar    -noverify   user.api\target\user.api-1.0-SNAPSHOT.jar
pause