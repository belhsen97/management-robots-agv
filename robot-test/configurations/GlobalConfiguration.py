import configparser

def read_config(fileName):
    # Create a ConfigParser object
    config = configparser.ConfigParser()
 
    config.read(fileName)
 
    debug_mode = config.getboolean('General', 'debug')
    log_level = config.get('General', 'log_level')
    broker = config.get('MQTT', 'broker')
    port = config.getint('MQTT', 'port')
 
    hosthttp = config.get('HTTP', 'host')
    porthttp = config.getint('HTTP', 'port')

    config_values = {
        'debug_mode': debug_mode,
        'log_level': log_level,
        'mqtt':{'host': broker,'port': port},
        'http':{'host': hosthttp,'port': porthttp}
    }
 
    return config_values
def create_config(fileName):
    config = configparser.ConfigParser()
 
    config['General'] = {'debug': True, 'log_level': 'info'}
    config['MQTT'] = {'broker': 'localhost','port': '1883'}
    config['HTTP'] = {'host': 'localhost','port': '8088'}

    with open(fileName, 'w') as configfile:
        config.write(configfile)