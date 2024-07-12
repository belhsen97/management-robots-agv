import json
from states import GlobalState as state
from models import NotificationModel as notification_model

def on_notification_change(notification:notification_model.ReactiveNotification):
    print(f"notification changed to {notification.toString()}")
    state.mqttState["service"].publish(state.mqttState["publish"]["notification"],notification.toSerialisation())
