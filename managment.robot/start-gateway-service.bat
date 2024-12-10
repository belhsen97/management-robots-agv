echo Start Trackbot
title Gateway-Service
color A0
mode con: cols=100 lines=25
java -jar    -noverify   gateway.api\target\gateway.api-1.0-SNAPSHOT.jar
pause