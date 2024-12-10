package tn.enova.Services.Interfaces;

import tn.enova.Enums.*;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Mappers.RobotMapper;
import tn.enova.Models.Commons.ConnectionInfo;
import tn.enova.Models.Entitys.Robot;
import tn.enova.Models.Entitys.RobotProperty;
import tn.enova.Repositorys.RobotPropertyRepository;
import tn.enova.Repositorys.RobotRepository;
import tn.enova.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;
    private final RobotPropertyRepository robotPropertyRepository;

    @Override
    public Robot selectByName( String name) {
        Optional<Robot> r = this.robotRepository.findbyName(name);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by id = " + name);
        }
        return r.get();
    }
    @Override
    public Robot selectByClientId( String clientId) {
        Optional<Robot> r = this.robotRepository.findbyClientId(clientId);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by clientId = " + clientId);
        }
        return r.get();
    }
    @Override
    public void updateConnection(ConnectionInfo connectionInfo ) {
        final Robot rDB = this.selectByClientId(connectionInfo.getClientId());
        Connection stateConnection;
        if (connectionInfo.getConnectedAt() >= connectionInfo.getDisconnectedAt()) {
            stateConnection = Connection.CONNECTED;
        } else {
            stateConnection = Connection.DISCONNECTED;
        }
        RobotProperty property = RobotProperty.builder()
                .name(connectionInfo.getClientId())
                .type(TypeProperty.CONNECTION)
                .timestamp(new Date())
                .value(stateConnection.toString())
                .build();
        rDB.setConnection(Connection.valueOf(stateConnection.toString()));
        robotRepository.save(rDB);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateConnection(String name, Object value) {
        Robot robot = this.selectByName(name);
        RobotProperty property = RobotProperty.builder()
                .name(name)
                .type(TypeProperty.CONNECTION)
                .timestamp(new Date())
                .value(value.toString())
                .build();
        robot.setConnection(Connection.valueOf(value.toString()));
        robotRepository.save(robot);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateStatusRobot(String name, Object value) {
        Robot robot = this.selectByName(name);
        RobotProperty property = RobotProperty.builder()
                .name(name)
                .type(TypeProperty.STATUS_ROBOT)
                .timestamp(new Date())
                .value(value.toString())
                .build();
        robot.setStatusRobot(StatusRobot.valueOf(value.toString()));
        robotRepository.save(robot);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateModeRobot(String name, Object value) {
        Robot robot = this.selectByName(name);
        RobotProperty property = RobotProperty.builder()
                .name(name)
                .type(TypeProperty.MODE_ROBOT)
                .timestamp(new Date())
                .value(value.toString())
                .build();
        robot.setModeRobot(ModeRobot.valueOf(value.toString()));
        robotRepository.save(robot);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateOperationStatus(String name, Object value) {
        Robot robot = this.selectByName(name);
        RobotProperty property = RobotProperty.builder()
                .name(name)
                .type(TypeProperty.OPERATION_STATUS)
                .timestamp(new Date())
                .value(value.toString())
                .build();
        robot.setOperationStatus(OperationStatus.valueOf(value.toString()));
        robotRepository.save(robot);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateLevelBattery(String name, Object value) {
        Robot robot = this.selectByName(name);
        RobotProperty property = RobotProperty.builder()
                .name(name)
                .type(TypeProperty.LEVEL_BATTERY)
                .timestamp(new Date())
                .value(value.toString())
                .build();
        robot.setLevelBattery(Double.parseDouble(value.toString()));
        robotRepository.save(robot);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateSpeed(String name, Object value) {
        Robot robot = this.selectByName(name);
        RobotProperty property = RobotProperty.builder()
                .name(name)
                .type(TypeProperty.SPEED)
                .timestamp(new Date())
                .value(value.toString())
                .build();
        robot.setSpeed(Double.parseDouble(value.toString()));
        robotRepository.save(robot);
        this.insertPropertyRobot(property);
    }

    @Override
    public void updateDistance(String name, Object value) {
        Robot robot = this.selectByName(name);
        robot.setDistance(Double.parseDouble(value.toString()));
        robotRepository.save(robot);
//        RobotProperty property = RobotProperty.builder()
//                .name(name)
//                .type(TypeProperty.DISTANCE)
//                .timestamp(new Date())
//                .value(value.toString())
//                .build();
//        this.insertPropertyRobot(property);
    }

    @Override
    public void updateTagCode(String name, Object value) {
        Robot robot = this.selectByName(name);
        robot.setCodeTag(value.toString());
        robotRepository.save(robot);
//        RobotProperty property = RobotProperty.builder()
//                .name(name)
//                .type(TypeProperty.TAGCODE)
//                .timestamp(new Date())
//                .value(value.toString())
//                .build();
//        this.insertPropertyRobot(property);
    }
    @Override
    public void insertPropertysRobot(List<RobotProperty> listPropertys) {
        for (RobotProperty property : listPropertys) {this.insertPropertyRobot(property);}
    }
    @Override
    public void  insertPropertyRobot(RobotProperty property){
        //if (robotPropertyRepository.countByType(property.getType()) == 0) {
        //property.setTimestamp(new Date());
        // robotPropertyRepository.save(property); continue;}
        final Optional<RobotProperty> rpOptional= robotPropertyRepository.findFirstByTypeOrderByTimestampDesc(property.getType());
        if (rpOptional.isEmpty()) {
            robotPropertyRepository.save(property);
            return;
        }
        if (!rpOptional.get().getValue().equals(property.getValue())) {
            robotPropertyRepository.save(property);
        }
    }
    @Override
    public void  updateRobotConnection( String clientId ,  Connection c) {
        Robot robot =  this.selectByClientId(clientId);
        robot.setConnection(c);
        if ( c == Connection.DISCONNECTED){
            robot.setOperationStatus(OperationStatus.PAUSE);
        }
        robotRepository.save(robot);
    }

    @Override
    public void  updateRobot( String name,  Robot robot) {
        final Robot robotDB = this.selectByName(name);
        if (robot.getStatusRobot() != null ){ robotDB.setStatusRobot(robot.getStatusRobot()); }
        if (robot.getModeRobot() != null ){ robotDB.setModeRobot(robot.getModeRobot()); }
        if (robot.getOperationStatus() != null ){ robotDB.setOperationStatus(robot.getOperationStatus()); }
        if (robot.getCodeTag() != null ){ robotDB.setCodeTag(robot.getCodeTag()); }
        robotDB.setLevelBattery(robot.getLevelBattery());
        robotDB.setSpeed(robot.getSpeed());
        robotDB.setDistance(robot.getDistance());
        List<RobotProperty> listPropertys = RobotMapper.convertToRobotPropertyList(robot);
        this.insertPropertysRobot(listPropertys);
    }
}

