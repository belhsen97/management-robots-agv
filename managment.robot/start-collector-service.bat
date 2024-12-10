echo Start Collector
title Collector-Service
color 70
mode con: cols=100 lines=25
java -jar    -noverify   collector.api\target\collector.api-1.0-SNAPSHOT.jar
pause