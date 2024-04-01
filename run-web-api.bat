echo Start web.api-0.0.1-SNAPSHOT
title web-api-Service
color F9
mode con: cols=100 lines=25
java -XX:MaxRAM=256m  -jar    -noverify  web.api\target\web.api-0.0.1-SNAPSHOT.jar
pause