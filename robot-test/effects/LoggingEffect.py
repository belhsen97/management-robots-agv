from states import GlobalState as state
from models import LoggingModel as logging_model
from enums import LoggingTypeEnum as logging_type_enum

def on_send_logging(logging:logging_model.ReactiveLogging):
    print(f"Logging changed to {logging.toString()}")
    state.mqttState["service"].publish(state.mqttState["publish"]["logging"],logging.toSerialisation())

def on_save_log_logging(logging:logging_model.ReactiveLogging):
    if logging.level ==  logging_type_enum.LevelType.DEBUG:
        state.logger.debug(logging.message)
        return
    if logging.level ==  logging_type_enum.LevelType.INFO:
        state.logger.info(logging.message)
        return
    if logging.level ==  logging_type_enum.LevelType.WARNING:
        state.logger.warning(logging.message)
        return
    if logging.level ==  logging_type_enum.LevelType.ERROR:
        state.logger.error(logging.message)
        return
    if logging.level ==  logging_type_enum.LevelType.TRACE:
        state.logger.critical(logging.message)
        return