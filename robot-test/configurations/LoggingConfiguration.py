import logging
from logging.handlers import RotatingFileHandler
import os

def logger_config(logName,log_directory = '', fileName = 'logging.log'):
    # Ensure the directory exists
    if log_directory != '':
       if not os.path.exists(log_directory):
          os.makedirs(log_directory)

    # Create a logger object
    logger = logging.getLogger(logName)
    logger.setLevel(logging.DEBUG)  # Set the logging level

    # Create a handler that writes log messages to a file, with rotation
    handler = RotatingFileHandler(
        os.path.join(log_directory, fileName), #'my_log.log',            # Log file name
        maxBytes=5 * 1024 * 1024,  # 5 MB per file
        backupCount=5             # Keep up to 5 backup files
    )

    # Create a logging format
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)

    # Add the handler to the logger
    logger.addHandler(handler)
    return logger


import glob

def read_all_logs(log_directory, fileName):
    # Get a list of all log files that match the pattern
    log_files = glob.glob(os.path.join(log_directory, fileName) + '*')

    log_contents = []
    for log_file in log_files:
        with open(log_file, 'r') as f:
            log_contents.append(f.read())

    return log_contents


# log_contents = read_all_logs("resources/logs","my_log.log")
# Print the contents of each log file
# for idx, content in enumerate(log_contents):
#     print(f"Contents of my_log.log.{idx}:\n{content}\n")

# logger = logger_config("robot-1","resources/logs","my_log.log")
# Example log messages
# for i in range(10000):
    # logger.debug(f'This is log message {i}')
    # logger.info('Info level log message')
    # logger.warning('Warning level log message')
    # logger.error('Error level log message')
    # logger.critical('Critical level log message')
