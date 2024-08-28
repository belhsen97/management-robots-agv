import configparser

def read_config(fileName):
    # Create a ConfigParser object
    config = configparser.ConfigParser()
 
    config.read(fileName)
 
    debug_mode = config.getboolean('LOG', 'debug')
    log_level = config.get('LOG', 'log_level')
    log_src = config.get('LOG', 'src')

    broker = config.get('MQTT', 'broker')
    port = config.getint('MQTT', 'port')
 
    hosthttp = config.get('HTTP', 'host')
    porthttp = config.getint('HTTP', 'port')

    config_values = {
        'LOG' : {'debug': debug_mode, 'level': log_level, 'src': log_src},
        'mqtt':{'host': broker,'port': port},
        'http':{'host': hosthttp,'port': porthttp}
    }
 
    return config_values
def create_config(fileName):
    config = configparser.ConfigParser()
 
    config['LOG'] = {'debug': True, 'level': 'info','src':'resources/logs'}
    config['MQTT'] = {'broker': 'localhost','port': '1883'}
    config['HTTP'] = {'host': 'localhost','port': '8088'}

    with open(fileName, 'w') as configfile:
        config.write(configfile)