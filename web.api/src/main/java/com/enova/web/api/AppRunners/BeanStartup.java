package com.enova.web.api.AppRunners;


import com.enova.web.api.Configures.ParameterConfig;
import com.enova.web.api.Enums.*;
import com.enova.web.api.Repositorys.*;
import com.enova.web.api.Services.FileService;
import com.enova.web.api.Services.TagService;
import com.enova.web.api.Models.Entitys.*;
import com.enova.web.api.Services.TaskSchedulingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Order(value = 1)//Register BeanRunnerOne bean
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanStartup implements CommandLineRunner {
    final UserRepository userRepository;
    final WorkstationRepository workstationRepository;
    final RobotRepository robotRepository;
    final FileService ifileService;
    final TagService iTagService;
    final PasswordEncoder passwordEncoder;
    final TraceRepository traceRepository;
    final ParameterConfig parameterConfig;
    final RobotSettingRepository repository;
    final RobotPropertyRepository robotPropertyRepository;
    final TaskSchedulingService taskSchedulingService;

    final MongoTemplate mongoTemplate;
    final Token token = Token.builder()
            .token("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3RmaTk3IiwiaWF0IjoxNzA5NDk0NTUyLCJleHAiOjE3MDk0OTU5OTJ9.8sxfA6qa8ijehu4GZJrnVaOQ5nCU9AXWYgtIQGAFsjs")
            .tokenType(TokenType.BEARER)
            .revoked(false).expired(false)
            .build();
    User userAdmin = User.builder()
            .firstname("belhsen")
            .lastname("bachouch")
            .enabled(true)
            .gender(Gender.MALE)
            .role(Roles.ADMIN)
            .matricule("1284953")
            .phone(55778547)
            .username("belhsen97")
            .password("1234")
            .createdAt(new Date())
            .email("belhsenbachouch97@gmail.com")
            .photo(Attachment.builder().fileName("filename").build())
            .build();

    List<RobotSetting> robotConfigs = Arrays.asList(
            RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MIN).value("1.5").unit("m/s").build(),
            RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MAX).value("8").unit("m/s").build(),
            RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MIN).value("0").unit("mm").build(),
            RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MAX).value("10").unit("mm").build()
    );

    @Override
    public void run(String... args) throws Exception {
        this.initAdminUser();
        this.verifyConfiGlobalRobot();
        //  this.deleteAndRandomAllTags(200);
        //  this.deleteAndRandomAllRobot(100);
        //  this.deleteAndRandomAllWorstation(20);
//        Date date = new Date();
//        date.setSeconds(date.getSeconds()+15);
//        taskSchedulingService.addScheduleTask(new Runnable () {
//            @Override
//            public void run() {
//                System.out.println("Scheduling task with job id: " );
//            }
//        },date);
//       String jobId =  taskSchedulingService.addScheduleTask(new Runnable () {
//            @Override
//            public void run() {
//                System.out.println("Scheduling task with job id: " );
//            }
//        },"*/10 * * * * *");
//        taskSchedulingService.removeScheduledTask(jobId);
    }


    private void deleteAndRandomAllTags(int number) {
        iTagService.deleteAll();
        log.info("finish add delete All Tag");
        for (int i = 1; i < number; i++) {
            final Tag tag = Tag.builder()
                    .code("code-" + i)
                    .description("description-tag")
                    .build();
            iTagService.insert(tag);
        }
        log.info("finish add All Tag");
    }

    private void deleteAndRandomAllRobot(int number) {
        robotRepository.deleteAll();
        log.info("finish add delete All  workstation");
        for (int i = 1; i <= number; i++) {
            final Robot robot = Robot.builder()
                    .name("robot-" + i)
                    .createdAt(new Date())
                    .connection(Connection.DISCONNECTED)
                    .modeRobot(ModeRobot.AUTO)
                    .statusRobot(StatusRobot.RUNNING)
                    .operationStatus(OperationStatus.PAUSE)
                    .levelBattery(100)
                    .speed(1.5)
                    .notice("")
                    .clientid("robot-" + i)
                    .username("robot-" + i)
                    .password("robot-" + i)
                    .build();
            robotRepository.save(robot);
        }
        log.info("finish add All robot");
    }

    private void deleteAndRandomAllWorstation(int number) {
        workstationRepository.deleteAll();
        for (int i = 1; i <= number; i++) {
            final Workstation workstation = Workstation.builder()
                    .name("workstation-" + i)
                    .enable(true)
                    .build();
            workstationRepository.save(workstation);
        }
    }

    private void verifyConfiGlobalRobot() {
        for (RobotSetting r : robotConfigs) {
            final Optional<RobotSetting> opt = this.repository.findRobotSettingByCategoryAndConstraint(r.getCategory(), r.getConstraint());
            if (opt.isEmpty()) {
                repository.save(r);
            }
        }
    }

    private void initAdminUser() throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(userAdmin.getUsername());
        if (userOptional.isEmpty()) {
            userAdmin.setPassword(passwordEncoder.encode(userAdmin.getPassword()));
            userAdmin.addToken(token);
            userAdmin = userRepository.save(userAdmin);
            final Attachment img = this.saveAttachment(parameterConfig.file_defaultUserPhoto);
            img.setId(userAdmin.getId());
            userAdmin.setPhoto(img);
            userAdmin = userRepository.save(userAdmin);
            log.info("finish add user : " + userAdmin.getUsername());
        }
    }

    Attachment saveAttachment(String pathFile) throws Exception {
        MultipartFile multipartFile = ifileService.importFileToMultipartFile(pathFile);
        String fileName = "";
        fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new Exception("Filename contains invalid path sequence "
                    + fileName);
        }
        Attachment attachment = Attachment.builder()
                .fileName(fileName)
                .fileType(multipartFile.getContentType())
                .fileSize(multipartFile.getSize())
                .data(multipartFile.getBytes())
                .build();
        return attachment;
    }


    static String generateRandomIP() {
        Random rand = new Random();
        StringBuilder ip = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ip.append(rand.nextInt(256));
            if (i < 3) {
                ip.append(".");
            }
        }
        return ip.toString();
    }
}

//        List<RobotProperty>  robotProperties = robotPropertyRepository.findAllByName("robot-1");
//        System.out.println(robotProperties.size());
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                 ObjectOutputStream oos = new ObjectOutputStream(bos);
//                oos.writeObject(robotProperties);
//                 System.out.println(bos.toByteArray().length);


// List<RobotProperty>  xxx = robotPropertyRepository.findAllByName("robot-200");
// for ( RobotProperty x: xxx){
//    x.setName("robot-20");
//  }
//   robotPropertyRepository.saveAll(xxx);
//  System.out.println(xxx.size());
//        traceRepository.deleteAll();
//        traceRepository.save(trace1);
//        log.info("finish add list of trace");
//
//
//

// robotRepository.updateWorkstation(workstation1.getName(),workstation3.getName());
// System.out.println( );
//  System.out.println(robotRepository.findAll().get(0).getWorkstation().getName());
//  System.out.println(workstationRepository.findAll().get(0).getRobots().size());
//  System.out.println(userRepository.  isExistsValidToken(token.getToken()));
//  System.out.println(userRepository.findTokenByTokenValue( token.getToken()));
//  token.setExpired(true);
//    System.out.println(userRepository.updateToken( token));
//      Optional<User> t =   userRepository.findUserByTokenValue(token.getToken()) ;
//        System.out.println(t.get().getFirstname());
//        Optional<User> u = userRepository.findTokensByUserId("65db0bd59d1807711d8ef371");
//System.out.println(u.orElse(null).getTokens().isEmpty());
//        userRepository.deleteAll();
//        for (User customer : userRepository.findAll()) {
//            System.out.println(customer);
//        }
//        System.out.println(userRepository.findByUsername( "belhsen97").get().getPhoto().getId());
//        System.out.println( userRepository.findUsersByRole(Roles.ADMIN).size());
