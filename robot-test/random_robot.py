import random
import time
from datetime import datetime, timedelta



# Initialize the connection_status variable
connection_status = random.choice(["CONNECTED", "DISCONNECTED"])
last_update_time = datetime.now()
# Function to update the connection status every 30 minutes
def update_connection_status():
    global connection_status, last_update_time
    # Get the current time
    current_time = datetime.now()
    # Check if 30 minutes have passed since the last update
    if current_time - last_update_time >= timedelta(minutes=30):
        # Update the connection status
        connection_status = random.choice(["CONNECTED", "DISCONNECTED"])
        # Update the last update time
        last_update_time = current_time


 




# Initialize variables
current_time = datetime.now()
status_intervals = [
    (0, 4),     # 0 to 4 hours: RUNNING
    (4, 5),     # 4 to 5 hours: INACTIVE
    (5, 5.5)    # 5 to 5.5 hours: WAITING
]
current_status_index = 0  # Index of the current status interval
status_transition_time = current_time
status_robot = "WAITING"

# Function to update the robot status
def update_robot_status():
    global current_time, current_status_index, status_transition_time, status_robot
    
    # Get the elapsed time since the start
    elapsed_time = (datetime.now() - current_time).total_seconds() / 3600  # Convert to hours
    
    # Check if it's time to transition to the next status interval
    if elapsed_time >= status_intervals[current_status_index][1]:
        # Move to the next status interval
        current_status_index = (current_status_index + 1) % len(status_intervals)
        # Set the transition time
        status_transition_time = datetime.now()
        
    # Update the status based on the current interval
    start_time, end_time = status_intervals[current_status_index]
    if start_time <= elapsed_time < end_time:
        if current_status_index == 0:
            status_robot = "RUNNING"
        elif current_status_index == 1:
            status_robot = "INACTIVE"
        elif current_status_index == 2:
            status_robot = "WAITING"







 
operation_intervals = [
    (0, 8),         # 0 to 8 hours: PAUSE
    (8, 8.1667)     # 8 to 8 hours and 10 minutes: EMS
]
current_operation_index = 0  # Index of the current operation interval
operation_transition_time = current_time
operation_status = "EMS"

# Function to update the operation status
def update_operation_status():
    global current_time, current_operation_index, operation_transition_time, operation_status
    
    # Get the elapsed time since the start
    elapsed_time = (datetime.now() - current_time).total_seconds() / 3600  # Convert to hours
    
    # Check if it's time to transition to the next operation interval
    if elapsed_time >= operation_intervals[current_operation_index][1]:
        # Move to the next operation interval
        current_operation_index = (current_operation_index + 1) % len(operation_intervals)
        # Set the transition time
        operation_transition_time = datetime.now()
        
    # Update the status based on the current interval
    start_time, end_time = operation_intervals[current_operation_index]
    if start_time <= elapsed_time < end_time:
        if current_operation_index == 0:
            operation_status = "PAUSE"
        elif current_operation_index == 1:
            operation_status = "EMS"






mode_intervals = [
    (0, 8),      # 0 to 8 hours: AUTO
    (8, 10)      # 8 to 10 hours: MANUAL
]
current_mode_index = 0  # Index of the current mode interval
mode_transition_time = current_time
mode_robot = "AUTO"

# Function to update the mode of the robot
def update_robot_mode():
    global current_time, current_mode_index, mode_transition_time, mode_robot
    
    # Get the elapsed time since the start
    elapsed_time = (datetime.now() - current_time).total_seconds() / 3600  # Convert to hours
    
    # Check if it's time to transition to the next mode interval
    if elapsed_time >= mode_intervals[current_mode_index][1]:
        # Move to the next mode interval
        current_mode_index = (current_mode_index + 1) % len(mode_intervals)
        # Set the transition time
        mode_transition_time = datetime.now()
        
    # Update the mode based on the current interval
    start_time, end_time = mode_intervals[current_mode_index]
    if start_time <= elapsed_time < end_time:
        if current_mode_index == 0:
            mode_robot = "AUTO"
        elif current_mode_index == 1:
            mode_robot = "MANUAL"






battery_level = 0
battery_direction = 1  # 1 for increasing, -1 for decreasing
last_update_time = datetime.now()
def update_battery_level():
    global battery_level, battery_direction, last_update_time
    elapsed_time = (datetime.now() - last_update_time).total_seconds()
    if elapsed_time >= 3600:  # 1 hour
        battery_direction = 1
        last_update_time = datetime.now()
    if elapsed_time >= 14400:  # 4 hours
        battery_direction = -1
        last_update_time = datetime.now()
    battery_level += battery_direction * 25 / 3600  # Step value: 25 / 3600 per second
    battery_level = max(min(battery_level, 100), 0)
  
    
speed_value = random.uniform(7.0, 8.0)
speed_timer = time.time()
is_first_30_min = True
# Function to update the speed value
def update_speed_value():
    global speed_value, speed_timer, is_first_30_min
    
    # Get the elapsed time since the start
    elapsed_time = time.time() - speed_timer
    
    # If it's within the first 30 minutes
    if elapsed_time <= 1800:
        # Change speed_value frequently
        speed_value = random.uniform(7.0, 9.0)
    else:
        # If it's the end of the first 30 minutes, stop changing speed for 1 minute
        if is_first_30_min:
            # Set is_first_30_min to False so it only runs once
            is_first_30_min = False
            # Sleep for 1 minute
            time.sleep(60)
            # Reset the speed_timer to start counting for the next interval
            speed_timer = time.time()


# from dataclasses import dataclass
# from enum import Enum

# @dataclass
# class Robot:
    # id: str
    # name: str
    # connection: str  # Assuming Connection is a string in this example
    # status_robot: 'StatusRobot'
    # mode_robot: 'ModeRobot'
    # operation_status: 'OperationStatus'
    # created_at: 'datetime.datetime'
    # level_battery: int
    # speed: float

# class StatusRobot(Enum):
    # WAITING = 1
    # RUNNING = 2
    # INACTIVE = 3

# class ResponseStatus(Enum):
    # SUCCESSFUL = 1
    # UNSUCCESSFUL = 2
    # ERROR = 3

# class OperationStatus(Enum):
    # EMS = 1
    # PAUSE = 2

# class ModeRobot(Enum):
    # AUTO = 1
    # MANUAL = 2
