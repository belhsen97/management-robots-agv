echo Start collector.api-0.0.1-SNAPSHOT
title collector.api-Service
color F9
mode con: cols=100 lines=25
java -XX:MaxRAM=256m -jar    -noverify  collector.api\target\collector.api-0.0.1-SNAPSHOT.jar
pause