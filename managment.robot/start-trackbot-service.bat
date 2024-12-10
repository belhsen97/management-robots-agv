echo Start Trackbot
title Trackbot-Service
color 70
mode con: cols=100 lines=25
java -jar    -noverify   trackbot.api\target\trackbot.api-1.0-SNAPSHOT.jar
pause