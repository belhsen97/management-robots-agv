import configparser

def read_config(fileName):
    # Create a ConfigParser object
    config = configparser.ConfigParser()
 
    # Read the configuration file
    config.read(fileName)
 
    # Access values from the configuration file
    debug_mode = config.getboolean('General', 'debug')
    log_level = config.get('General', 'log_level')
    broker = config.get('MQTT', 'broker')
    port = config.getint('MQTT', 'port')
 
    # Return a dictionary with the retrieved values
    config_values = {
        'debug_mode': debug_mode,
        'log_level': log_level,
        'broker': broker,
        'port': port
    }
 
    return config_values
def create_config(fileName):
    config = configparser.ConfigParser()
 
    # Add sections and key-value pairs
    config['General'] = {'debug': True, 'log_level': 'info'}
    config['MQTT'] = {'broker': 'localhost','port': '1883'}
 
    # Write the configuration to a file
    with open(fileName, 'w') as configfile:
        config.write(configfile)