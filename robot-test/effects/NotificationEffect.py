import json
from states import GlobalState as state
from models import NotificationModel as notification_model
from enums import NotificationEnum as notification_enum

def on_send_notification(notification:notification_model.ReactiveNotification):
    print(f"notification changed to {notification.toString()}")
    state.mqttState["service"].publish(state.mqttState["publish"]["notification"],notification.toSerialisation())

def on_save_log_notification(notification:notification_model.ReactiveNotification):
    if notification.level ==  notification_enum.LevelType.DEBUG:
        state.logger.debug(notification.message)
        return
    if notification.level ==  notification_enum.LevelType.INFO:
        state.logger.info(notification.message)
        return
    if notification.level ==  notification_enum.LevelType.WARNING:
        state.logger.warning(notification.message)
        return
    if notification.level ==  notification_enum.LevelType.ERROR:
        state.logger.error(notification.message)
        return
    if notification.level ==  notification_enum.LevelType.TRACE:
        state.logger.critical(notification.message)
        return