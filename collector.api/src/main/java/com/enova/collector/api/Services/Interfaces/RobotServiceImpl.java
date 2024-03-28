package com.enova.collector.api.Services.Interfaces;


import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;
import com.enova.collector.api.Exceptions.RessourceNotFoundException;
import com.enova.collector.api.Mappers.RobotMapper;
import com.enova.collector.api.Repositorys.RobotPropertyRepository;
import com.enova.collector.api.Repositorys.RobotRepository;
import com.enova.collector.api.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;
    private final RobotPropertyRepository robotPropertyRepository;
    private final RobotMapper robotMapper;
    @Override
    public void insertDataPropertys( Robot robot) {
        final List<RobotProperty> listPropertys = robotMapper.convertToRobotPropertyList(robot);

        for (RobotProperty property : listPropertys) {
            /*if (robotPropertyRepository.countByType(property.getType()) == 0) {
                //property.setTimestamp(new Date());
                robotPropertyRepository.save(property);
                continue;
            }*/
            final Optional<RobotProperty> rpOptional= robotPropertyRepository.findFirstByTypeOrderByTimestampDesc(property.getType());
            if (rpOptional.isEmpty()) {
                robotPropertyRepository.save(property);
                continue;
            }
            if (!rpOptional.get().getValue().equals(property.getValue())) {
                robotPropertyRepository.save(property);
            }
        }
    }
    @Override
    public void  updateRobot( Robot r,  Robot ru) {
        r.setConnection(ru.getConnection());
        r.setStatusRobot(ru.getStatusRobot());
        r.setModeRobot(ru.getModeRobot());
        r.setOperationStatus(ru.getOperationStatus());
        r.setLevelBattery(ru.getLevelBattery());
        r.setSpeed(ru.getSpeed());
        robotRepository.save(r);
    }

    @Override
    public Robot selectById( String id) {
        Optional<Robot> r = this.robotRepository.findById(id);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by id = " + id);
        }
        return r.get();
    }

    @Override
    public Robot selectByName( String name) {
        Optional<Robot> r = this.robotRepository.findbyName(name);
        System.out.println(r.isPresent());
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by id = " + name);
        }
        return r.get();
    }

    @Override
    public Robot update( String id, Robot obj) {
        Robot r = this.selectById(id);
        r.setConnection(obj.getConnection());
        r.setStatusRobot(obj.getStatusRobot());
        r.setModeRobot(obj.getModeRobot());
        r.setOperationStatus(obj.getOperationStatus());
        r.setLevelBattery(obj.getLevelBattery());
        r.setSpeed(obj.getSpeed());
        r = robotRepository.save(r);
        return r;
    }

    @Override
    public Robot updatebyName( String name,  Robot ru) {
        Robot r = this.selectByName(name);
        r.setConnection(ru.getConnection());
        r.setStatusRobot(ru.getStatusRobot());
        r.setModeRobot(ru.getModeRobot());
        r.setOperationStatus(ru.getOperationStatus());
        r.setLevelBattery(ru.getLevelBattery());
        r.setSpeed(ru.getSpeed());
        r = robotRepository.save(ru);
        return r;
    }


}

