echo Start Configure
title Configure-Service
color 17
mode con: cols=100 lines=25
java -jar -noverify configure.api\target\configure.api-1.0-SNAPSHOT.jar
pause