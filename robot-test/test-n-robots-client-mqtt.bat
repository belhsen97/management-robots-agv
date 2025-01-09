@echo off
set /p num_robots="Enter the number of robots: "

for /l %%i in (1, 1, %num_robots%) do (
 start cmd.exe /c "python main.py -name=robot-%%i"
)

pause