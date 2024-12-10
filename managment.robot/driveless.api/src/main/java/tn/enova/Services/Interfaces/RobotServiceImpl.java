package tn.enova.Services.Interfaces;



import tn.enova.Enums.*;
import tn.enova.Exceptions.RessourceFoundException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Commons.*;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enova.States.GlobalState;

import java.util.List;
import java.util.Optional;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {
    private final TagService tagService;
    private final RobotSettingService robotSettingService;
    private final  NotificationService notificationService;
    private final OrderService orderService;
    private Optional<Robot> findbyClientId(String clientId) {
        return GlobalState.listRobots.stream()
                .filter(r -> r.getClientid().equals(clientId))
                .findFirst();
    }
    private Optional<Robot> findbyName(String name) {
        return GlobalState.listRobots.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();
    }
    private Optional<Robot> findById(String id) {
        return GlobalState.listRobots.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Robot> selectAll() {
        return GlobalState.listRobots;
    }
    @Override
    public Robot selectById(String id) {
        Optional<Robot> r = this.findById(id);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Robot by id = " + id);
        }
        return r.get();
    }
    @Override
    public Optional<Robot> selectByUsernameAndPassword(String username, String password) {
        return GlobalState.listRobots.stream()
                .filter(r -> r.getUsername().equals(username) && r.getPassword().equals(password))
                .findFirst();
    }
    @Override
    public Robot selectByClientId( String clientId) {
        Optional<Robot> r = this.findbyClientId(clientId);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by clientId = " + clientId);
        }
        return r.get();
    }
    @Override
    public Robot selectByName(String name) {
        Optional<Robot> r =   this.findbyName(name);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by name = " + name);
        }
        return r.get();
    }
    @Override
    public Robot insert(Robot robot) {
        Optional<Robot> r = this.findById(robot.getId());
        if (r.isPresent()) {
            throw new RessourceFoundException("found Robot by id = " + robot.getId());
        }
        GlobalState.listRobots.add(robot);
        return robot;
    }
    @Override
    public void insert(List<Robot> list) {
        for (Robot r : list) {
            this.insert(r);
        }
    }
    @Override
    public Robot update(String id, Robot updatedRobot) {
        Robot existingRobot = selectById(id);
        if (updatedRobot.getCodeTag() != null && updatedRobot.getCodeTag() != ""){
//            Optional<Tag> tag = tagService.selectByCode(updatedRobot.getCodeTag());
//            if ( tag.isPresent()) {
//                existingRobot.setCodeTag(tag.get().getCode());
//            }
           Tag  tag = tagService.selectByCode(updatedRobot.getCodeTag());
           if (tag != null)  {  existingRobot.setCodeTag(tag.getCode());   }
        }
        if ((updatedRobot.getName() != null ? !updatedRobot.getName().equals(existingRobot.getName()) : false)){
            existingRobot.setName(updatedRobot.getName());
        }
        if (updatedRobot.getClientid() != null){existingRobot.setClientid(updatedRobot.getClientid());}
        if (updatedRobot.getUsername() != null){existingRobot.setUsername(updatedRobot.getUsername());}
        if (updatedRobot.getPassword() != null){existingRobot.setPassword(updatedRobot.getPassword());}
        return existingRobot;
    }


    @Override
    public Robot  updateRobotConnection( ConnectionInfo connectionInfo) {
        NotificationResponse notificationResponse = NotificationResponse.builder().build();
        Connection stateConnection;

        if (connectionInfo.getConnectedAt() >= connectionInfo.getDisconnectedAt()) {
            stateConnection = Connection.CONNECTED;
            notificationResponse.setTimestamp(connectionInfo.getConnectedAt() );
            notificationResponse.setLevel(LevelType.INFO);
        } else {
            notificationResponse.setTimestamp(connectionInfo.getDisconnectedAt() );
            stateConnection = Connection.DISCONNECTED;
            notificationResponse.setLevel(LevelType.WARNING);
        }

        Robot r =  this.selectByClientId(connectionInfo.getClientId());
        if ( r.getConnection() != stateConnection ){
            r.setConnection(stateConnection);
            notificationResponse.setFrom(r);
            notificationResponse.setMessage("robot  "+r.getName()+" is  "+stateConnection.name());
            notificationService.notify(notificationResponse);
        }
        return r;
    }

    @Override
    public Robot  updateRobotStatus( String nameRobot ,  StatusRobot s)  {
        Robot r =  this.selectByName(nameRobot);
        if ( r.getStatusRobot() != s){
            NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                    .message("Status of robot  "+r.getName()+" is  "+s.name()).build();
            if (s == StatusRobot.INACTIVE){notificationResponse.setLevel(LevelType.WARNING);}
            else if (s == StatusRobot.RUNNING){notificationResponse.setLevel(LevelType.INFO);}
            else {notificationResponse.setLevel(LevelType.INFO);}
            notificationService.notify(notificationResponse);
            r.setStatusRobot(s);
        }
        return r;
    }
    @Override
    public Robot  updateRobotMode( String nameRobot ,  ModeRobot m)  {
        Robot r =  this.selectByName(nameRobot);
        if ( r.getModeRobot() != m){
            NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                    .message("Mode of robot  "+r.getName()+" is  "+m.name()).build();
            if (m == ModeRobot.MANUAL){notificationResponse.setLevel(LevelType.WARNING);}
            else {notificationResponse.setLevel(LevelType.INFO);}
            notificationService.notify(notificationResponse);
            r.setModeRobot(m);
        }
        return r;
    }
    @Override
    public Robot  updateRobotOperationStatus( String nameRobot ,  OperationStatus o) {
        Robot r =  this.selectByName(nameRobot);
        if ( r.getOperationStatus() != o){
            NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                    .message("Operation status of robot  "+r.getName()+" is  "+o.name()).build();
            if (o == OperationStatus.EMS){notificationResponse.setLevel(LevelType.WARNING);}
            else if (o == OperationStatus.PAUSE){notificationResponse.setLevel(LevelType.WARNING);}
            else {notificationResponse.setLevel(LevelType.INFO);}
            notificationService.notify(notificationResponse);
            r.setOperationStatus(o);
        }
        return r;
    }
    @Override
    public Robot  updateRobotLevelBattery( String nameRobot ,  double value) {
        Robot r =  this.selectByName(nameRobot);
        if ( (value == 100l) && (value != r.getSpeed())  ){
            NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                                                                                      .level(LevelType.WARNING)
                                                                                      .message("robot "+r.getName()+" is Full charge")
                                                                                      .build();
            notificationService.notify(notificationResponse);
        }
        if ( ( value == 0l )  && (value != r.getSpeed())){
            NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                                                                                      .level(LevelType.WARNING)
                                                                                      .message("robot "+r.getName()+" is Low power will be INACTIVE")
                                                                                      .build();
            notificationService.notify(notificationResponse);
        }
        r.setLevelBattery(value);
        return r;
    }
    @Override
    public Robot  updateRobotSpeed( String nameRobot ,  double value) {
        Robot r =  this.selectByName(nameRobot);

        List<RobotSetting> listSetting = this.robotSettingService.selectByTypeProperty(TypeProperty.SPEED);
        for ( RobotSetting robotSetting :  listSetting){
            if ( robotSetting.getConstraint() ==  Constraint.MIN ){
                double minSpeed = Double.parseDouble(robotSetting.getValue());
                if ( (minSpeed > value) &&  !(minSpeed > r.getSpeed())  ){
                    NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                                                                                              .level(LevelType.WARNING)
                                                                                              .message("speed of robot "+r.getName()+" is over than "+robotSetting.getValue()+robotSetting.getUnit())
                                                                                              .build();
                    notificationService.notify(notificationResponse);
               }
            }
            if ( robotSetting.getConstraint() ==  Constraint.MAX ){
                double maxSpeed = Double.parseDouble(robotSetting.getValue());
                if ( (maxSpeed < value) &&  !(maxSpeed < r.getSpeed())  ){
                    NotificationResponse notificationResponse = NotificationResponse.builder().from(r)
                                                                                              .level(LevelType.WARNING)
                                                                                              .message("speed of robot "+r.getName()+" is over than "+robotSetting.getValue()+robotSetting.getUnit())
                                                                                              .build();
                    notificationService.notify(notificationResponse);
                }
            }
        }

        r.setSpeed(value);
        return r;
    }
    @Override
    public Robot  updateRobotTagCode( String nameRobot ,  String code) {
        Robot r =  this.selectByName(nameRobot);
        Tag  tag = tagService.selectByCode(code);
        if (tag != null)  { r.setCodeTag(code);}
        return r;
    }
    @Override
    public void  updateAllRobotsStatus( StatusRobot newStatus) {
    }
    @Override
    public void  updateAllRobotsMode( ModeRobot newMode) {
        orderService.send(TypeProperty.MODE_ROBOT,newMode.name());
    }
    @Override
    public void  updateAllRobotsOperationStatus( OperationStatus newOperationStatus) {
        orderService.send(TypeProperty.OPERATION_STATUS,newOperationStatus.name());
    }
    @Override
    public void  updateAllRobotsSpeed( double value) {
        orderService.send(TypeProperty.SPEED,value);
    }









    @Override
    public void delete(String id) {
        boolean removed = GlobalState.listRobots.removeIf(r -> r.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Robot not found with id: " + id);
        }
    }
    @Override
    public void deleteAll() {
        GlobalState.listRobots.clear();
    }
}

