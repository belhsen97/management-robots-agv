package com.enova.web.api.appRunners;

import com.enova.web.api.Entitys.*;
import com.enova.web.api.Entitys.Enums.*;
import com.enova.web.api.Repositorys.RobotRepository;
import com.enova.web.api.Repositorys.UserRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.IFileService;
import com.enova.web.api.Services.Interfaces.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;
import java.util.Random;


@Order(value = 1)//Register BeanRunnerOne bean
@Slf4j
@Component
public class bean_ResetData implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkstationRepository workstationRepository;
    @Autowired
    RobotRepository robotRepository;
    @Autowired
    IFileService ifileService;
    @Autowired
    PasswordEncoder passwordEncoder;
    final Token token = Token.builder()
            .token("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3RmaTk3IiwiaWF0IjoxNzA5NDk0NTUyLCJleHAiOjE3MDk0OTU5OTJ9.8sxfA6qa8ijehu4GZJrnVaOQ5nCU9AXWYgtIQGAFsjs")
            .tokenType(TokenType.BEARER)
            .revoked(false).expired(false)
            .build();
    final User user = User.builder()
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


    @Override
    public void run(String... args) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            user.setPassword(passwordEncoder.encode("1234"));
            final Attachment img = this.saveAttachment(FileService.defaultUserPhoto);
            img.setId("12345678");
            user.setPhoto(img);
            user.addToken(token);
            userRepository.save(user);
        }
        workstationRepository.deleteAll();
        workstation1 = workstationRepository.save(workstation1);
        workstation2 = workstationRepository.save(workstation2);
        workstation3 = workstationRepository.save(workstation3);
        robotRepository.deleteAll();
        for ( int i = 1; i<26; i++) {
            final Robot robot = Robot.builder()
                    //.id(i)
                    .idWorkstation(workstation1.getName())
                    //.workstation(workstation1)
                    .name("robot-"+i)
                    .createdAt(new Date())
                    .connection(Connection.CONNECTED)
                    .modeRobot(ModeRobot.AUTO)
                    .statusRobot(StatusRobot.RUNNING)
                    .operationStatus(OperationStatus.PAUSE)
                    .levelBattery(100)
                    .speed(1.5)
                    .build();
            robotRepository.save(robot);
        }
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
//
//        System.out.println( userRepository.findUsersByRole(Roles.ADMIN).size());
    }


    public Attachment saveAttachment(String pathFile) throws Exception {
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

        public static String generateRandomIP() {
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





/*
{
  from: 'workstation',
  localField: 'idWorkstation',
  foreignField: 'name',
  as: 'workstation'
}
* */