package com.enova.collector.api;

import com.enova.collector.api.Configures.MQTTClientConfig;
import com.enova.collector.api.Enums.Connection;
import com.enova.collector.api.Enums.ModeRobot;
import com.enova.collector.api.Enums.OperationStatus;
import com.enova.collector.api.Enums.StatusRobot;
import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Services.Interfaces.MQTTServiceImpl;
import com.enova.collector.api.Services.MQTTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ApplicationTests {


	@Test
	void contextLoads() {

		}





}
