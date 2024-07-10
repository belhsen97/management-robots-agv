package com.enova.driveless.api.AppRunners;

import com.enova.driveless.api.Mappers.RobotSettingMapper;
import com.enova.driveless.api.Models.Commons.Publish;
import com.enova.driveless.api.Models.Commons.RobotSettingCommon;
import com.enova.driveless.api.Models.Entitys.RobotSetting;
import com.enova.driveless.api.Repositorys.RobotSettingRepository;
import com.enova.driveless.api.Services.MQTTService;
import com.enova.driveless.api.Services.RobotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Order(value = 1)
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanStartup implements CommandLineRunner {
    final RobotService robotService;
    @Override
    public void run(String... args) {
        List<RobotSetting> list =   robotService.selectAllRobotSetting();
        RobotSettingCommon robotSettingCommon = RobotSettingMapper.mapToDto(list);
       //System.out.println(Arrays.toString(list.toArray()));
        System.out.println( robotSettingCommon  );
    }
}