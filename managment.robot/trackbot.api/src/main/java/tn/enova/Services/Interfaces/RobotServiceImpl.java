package tn.enova.Services.Interfaces;


import tn.enova.Enums.LevelType;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
import tn.enova.Exceptions.MethodArgumentNotValidException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Entitys.Robot;
import tn.enova.Models.Entitys.RobotProperty;
import tn.enova.Models.Entitys.Tag;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Repositorys.RobotPropertyRepository;
import tn.enova.Repositorys.RobotRepository;
import tn.enova.Services.DrivelessService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.RobotService;
import tn.enova.Services.TagService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;
    private final RobotPropertyRepository robotPropertyRepository;
    private final TagService tagService;
    private final MongoTemplate mongoTemplate;
    private final DrivelessService drivelessService;
    private final NotificationService notificationService;
    @Override
    public List<Robot> selectAll() {
        return this.robotRepository.findAll();
    }
    @Override
    public List<Robot> selectAllByIds(List<String> ids) {
        return (List<Robot>) this.robotRepository.findAllById(ids);
    }
    @Override
    public Robot selectById(String id) {
        Optional<Robot> r = this.robotRepository.findById(id);
        if (r.isEmpty()) {throw new RessourceNotFoundException("Cannot found robot by id = " + id);}
        return r.get();
    }


    @Override
    public Robot selectByName(String name) {
        Optional<Robot> r = this.robotRepository.findByName(name);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by name = " + name);
        }
        return r.get();
    }

    @Override
    public List<RobotProperty> selectDataPropertysAllOrByNameOrUnixTimestamps(String name, Long startUnixTimestamp, Long endUnixTimestamp){
        if ( name == null ){return  this.robotPropertyRepository.findAll();}
        if ( startUnixTimestamp == null &&  endUnixTimestamp == null  ){return  this.robotPropertyRepository.findAllByName(name);}
        Date start;
        Date end;
        if (startUnixTimestamp == null ){
            end = new Date(endUnixTimestamp);
            return  this.robotPropertyRepository.findByNameAndTimestamp( name,  end);
        }
        if (endUnixTimestamp == null ){
            start = new Date(startUnixTimestamp);
            return  this.robotPropertyRepository.findByNameAndTimestamp( name,  start);
        }
         start = new Date(startUnixTimestamp);
         end = new Date(endUnixTimestamp);
        return  this.robotPropertyRepository.findByNameAndTimestampBetween( name,  start,  end);
    }
    @Override
    public Robot insert(Robot robot) {
        if ( this.robotRepository.findByName(robot.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Robot found equal " + robot.getName());
        }
        robot.setCreatedAt(new Date());
        if (robot.getCodeTag() != null && robot.getCodeTag() != ""){
            Tag tag = tagService.selectByCode(robot.getCodeTag());
            Optional<Robot> optRobot =  this.robotRepository.findRobotByCodeTag(robot.getCodeTag());
            if (optRobot.isPresent() ){
                throw new MethodArgumentNotValidException("other robot is use code : " + robot.getCodeTag());
            }
            robot.setCodeTag(tag.getCode());
        }
        else {robot.setCodeTag(null);}
        robot.setStatusRobot(StatusRobot.WAITING);
        robot.setOperationStatus(OperationStatus.PAUSE);
        robot.setModeRobot(ModeRobot.AUTO);
        robot =  this.robotRepository.save(robot);
        CompletableFuture<?> insertRobotDriveless = this.drivelessService.insertRobot(robot);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("add new robot where is name = "+robot.getName()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("add new robot where is name = "+robot.getName()).build());
        CompletableFuture.allOf(insertRobotDriveless, notify1,notify2);
        return robot;
    }

    @Override
    public Robot update(String id, Robot obj) {
        Robot r = this.selectById(id);
        r.setNotice(obj.getNotice());
        if (obj.getCodeTag() != null && obj.getCodeTag() != ""){
            Tag tag = tagService.selectByCode(obj.getCodeTag());
            r.setCodeTag(tag.getCode());
        }
        if ((obj.getName() != null ? !obj.getName().equals(r.getName()) : false)){
            r.setName(obj.getName());
            this.updateMultipleNameRobotPropertys (r.getName(),obj.getName());
        }
        if (obj.getClientid() != null){r.setClientid(obj.getClientid());}
        if (obj.getUsername() != null){r.setUsername(obj.getUsername());}
        if (obj.getPassword() != null){r.setPassword(obj.getPassword());}
        //r.setStatusRobot(obj.getStatusRobot());
        //r.setModeRobot(obj.getModeRobot());
        //r.setOperationStatus(obj.getOperationStatus());
        r =  this.robotRepository.save(r);
        CompletableFuture<?> updateRobotDriveless =   this.drivelessService.updateRobot(id,r);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("update robot where is name = "+r.getName()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("update robot where is name = "+r.getName()).build());
        CompletableFuture.allOf(updateRobotDriveless, notify1,notify2);
        return r;
    }


    @Override
    public void delete(String id) {
        Robot r = this.selectById(id);
        this.deleteMultipleRobotPropertysByName ( r.getName() );
        this.robotRepository.delete(r);
        CompletableFuture<?> deleteRobotDriveless = this.drivelessService.deleteRobot(id);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("delete robot where is name = "+r.getName()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("delete robot where is name = "+r.getName()).build());
        CompletableFuture.allOf(deleteRobotDriveless, notify1,notify2);
    }

    @Override
    public void deleteAll() {
        this.robotRepository.deleteAll();
        CompletableFuture<?> deleteAllRobotsDriveless = this.drivelessService.deleteAllRobots();
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("delete all robots").build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("delete all robots").build());
        CompletableFuture.allOf(deleteAllRobotsDriveless, notify1,notify2);
   }

    private void deleteMultipleRobotPropertysByName ( String name ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        DeleteResult deleteResult = mongoTemplate.remove(query, RobotProperty.class);
        if (deleteResult.getDeletedCount() == 0) {
            throw new MethodArgumentNotValidException("Cannot delete mutiple property of robot may be is empty or name not correct : " + name);
        }
    }
    private void updateMultipleNameRobotPropertys ( String oldName  , String newName ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(oldName));
        Update update = new Update();
        update.set("name", newName);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, RobotProperty.class);
        if (updateResult.getModifiedCount() == 0) {
            throw new MethodArgumentNotValidException("Cannot update mutiple name property of robot may be is empty or old name not correct or the same new name");
        }
    }
}