package tn.enova.AppRunners;



import tn.enova.Enums.*;
import tn.enova.Models.Entitys.*;
import tn.enova.Repositorys.*;
import tn.enova.Services.TagService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;


@Order(value = 1)
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanStartup implements CommandLineRunner {
    final WorkstationRepository workstationRepository;
    final RobotRepository robotRepository;
    final TagService iTagService;
    final RobotSettingRepository robotSettingRepository;

    final MongoTemplate mongoTemplate;

    List<RobotSetting> robotConfigs = Arrays.asList(
            RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MIN).value("0.1").unit("m/s").build(),
            RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MAX).value("0.3").unit("m/s").build(),
            RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MIN).value("0.1").unit("m").build(),
            RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MAX).value("1").unit("m").build()
    );

    @Override
    public void run(String... args) throws Exception {
        this.verifyConfiGlobalRobot();
       // this.deleteAndRandomAllTags(400);
       // this.deleteAndRandomAllRobot(100);
       // this.deleteAndRandomAllWorstation(20);
    }

    private void deleteAndRandomAllTags(int number) {
        this.iTagService.deleteAll();
        log.info("finish add delete All Tag");
        int firstPart = 0;
        int secondPart = 0;
        for (int i = 1; i < number; i++) {
                if (secondPart > 9999) {secondPart = 0;firstPart++;}
                String firstPartFormatted  = String.format("%04d", 0);
                String secondPartFormatted  = String.format("%04d", i);
                String codeFormat = String.format("%s-%s-%s", "code", firstPartFormatted, secondPartFormatted);
                final Tag tag = Tag.builder()
                        .code(codeFormat)
                        .workstation(null)
                        .build();
            this.iTagService.insert(tag);
        }
        log.info("finish add All Tag");
    }

    private void deleteAndRandomAllRobot(int number) {
        this.robotRepository.deleteAll();
        log.info("finish add delete All  workstation");
        for (int i = 1; i <= number; i++) {
            final Robot robot = Robot.builder()
                    .name("robot-" + i)
                    .createdAt(new Date())
                    .notice("")
                    .clientid("robot-" + i)
                    .username("robot-" + i)
                    .password("robot-" + i)

                    .connection(Connection.DISCONNECTED)
                    .modeRobot(ModeRobot.AUTO)
                    .statusRobot(StatusRobot.WAITING)
                    .operationStatus(OperationStatus.PAUSE)
                    .levelBattery(100.0)
                    .speed(0.0)
                    .distance(0.0)
                    //.codeTag("")
                    .build();
            this.robotRepository.save(robot);
        }
        log.info("finish add All robot");
    }

    private void deleteAndRandomAllWorstation(int number) {
        this.workstationRepository.deleteAll();
        for (int i = 1; i <= number; i++) {
            final Workstation workstation = Workstation.builder()
                    .name("workstation-" + i)
                    .description("workstation-" + i)
                    .enable(true)
                    .build();
            this.workstationRepository.save(workstation);
        }
    }

    private void verifyConfiGlobalRobot() {
        for (RobotSetting r : robotConfigs) {
            final Optional<RobotSetting> opt = this.robotSettingRepository.findRobotSettingByCategoryAndConstraint(r.getCategory(), r.getConstraint());
            if (opt.isEmpty()) {
                this.robotSettingRepository.save(r);
            }
        }
    }
}