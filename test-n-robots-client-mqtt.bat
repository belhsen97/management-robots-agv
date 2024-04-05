@echo off
for /l %%i in (1, 1, 1) do (
    start cmd.exe /c "python test.api/robot-client-mqtt.py -name=robot-%%i"
)
pause