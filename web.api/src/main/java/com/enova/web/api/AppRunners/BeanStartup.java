package com.enova.web.api.AppRunners;


import com.enova.web.api.Configures.ParameterConfig;
import com.enova.web.api.Enums.*;
import com.enova.web.api.Repositorys.*;
import com.enova.web.api.Services.FileService;
import com.enova.web.api.Services.TagService;
import com.enova.web.api.Models.Entitys.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;




import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;





import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;



import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
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
    final RobotPropertyRepository   robotPropertyRepository;

    final MongoTemplate mongoTemplate;
    final Token token = Token.builder()
            .token("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3RmaTk3IiwiaWF0IjoxNzA5NDk0NTUyLCJleHAiOjE3MDk0OTU5OTJ9.8sxfA6qa8ijehu4GZJrnVaOQ5nCU9AXWYgtIQGAFsjs")
            .tokenType(TokenType.BEARER)
            .revoked(false).expired(false)
            .build();
    User user = User.builder()
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

    Workstation workstation1 = Workstation.builder()
            .name("workstation-1")
            .enable(true)
            .build();
    Workstation workstation2 = Workstation.builder()
            .name("workstation-2")
            .enable(true)
            .build();
    Workstation workstation3 = Workstation.builder()
            .name("workstation-3")
            .enable(true)
            .build();
    Tag tag1 = Tag.builder().code("code-1").description("description").build();
    Trace trace1 = Trace.builder().username(user.getUsername()).timestamp(new Date()).className("RobotService").methodName("insert").description("add new robot where is name = robot-1").build();
    List<RobotSetting> robotConfigs = Arrays.asList(
            RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MIN).value("1.5").unit("m/s").build(),
            RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MAX).value("8").unit("m/s").build(),
            RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MIN).value("0").unit("mm").build(),
            RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MAX).value("10").unit("mm").build()
    );

    @Override
    public void run(String... args) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            user.setPassword(passwordEncoder.encode("1234"));
            user.addToken(token);
            user = userRepository.save(user);
            final Attachment img = this.saveAttachment(parameterConfig.file_defaultUserPhoto);
            img.setId(user.getId());
            user.setPhoto(img);
            user = userRepository.save(user);
            log.info("finish add user : " + user.getUsername());
        }
        for ( RobotSetting r : robotConfigs){
            final Optional<RobotSetting> opt = this.repository.findRobotSettingByCategoryAndConstraint(r.getCategory(),r.getConstraint());
            if (opt.isEmpty()) {
                repository.save(r);
            }
         }

        List<RobotProperty>  robotProperties = robotPropertyRepository.findAllByName("robot-1");
        System.out.println(robotProperties.size());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(robotProperties);
                 System.out.println(bos.toByteArray().length);


        createFile("test.h5");




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
//        workstationRepository.deleteAll();
//        workstation1 = workstationRepository.save(workstation1);
//        workstation2 = workstationRepository.save(workstation2);
//        workstation3 = workstationRepository.save(workstation3);
//        iTagService.deleteAll();
//        for (int i = 1; i < 100; i++) {
//            final Tag tag = Tag.builder()
//                    .code("code-" + i)
//                    .description("description-tag")
//                    .workstationName(workstation1.getName())
//                    .build();
//            iTagService.insert(tag);
//        }
//        log.info("finish add list of workstation");
//        robotRepository.deleteAll();
//        for (int i = 1; i <= 50; i++) {
//            final Robot robot = Robot.builder()
//                    .nameWorkstation(workstation1.getName())
//                    .name("robot-"+i)
//                    .createdAt(new Date())
//                    .connection(Connection.DISCONNECTED)
//                    .modeRobot(ModeRobot.AUTO)
//                    .statusRobot(StatusRobot.RUNNING)
//                    .operationStatus(OperationStatus.PAUSE)
//                    .levelBattery(100)
//                    .speed(1.5)
//                    .notice("")
//                    .clientid("robot-"+i)
//                    .username("robot-"+i)
//                    .password("robot-"+i)
//                    .build();
//            robotRepository.save(robot);
//        }
//        log.info("finish add list of robot");
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

void createFile(String fname){
    int file_id = -1;

    // Load the HDF5 library
    try {
        System.loadLibrary("jhdf5"); // Load the native HDF5 library
    } catch (UnsatisfiedLinkError e) {
        e.printStackTrace();
        System.err.println("Failed to load HDF5 native library.");
        return;
    }

    // Create a new file using default properties.
    try {
        file_id = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Failed to create file:" + fname);
        return;
    }

    // Close the file.
    try {
        if (file_id >= 0)
            H5.H5Fclose(file_id);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}