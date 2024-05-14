package com.enova.collector.api.Services.Interfaces;


import com.enova.collector.api.Enums.Connection;
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

    @Override
    public void insertDataPropertys(List<RobotProperty> listPropertys) {

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
    public void  updateRobotConnection( String clientId ,  Connection c) {
        Robot r =  this.selectByClientId(clientId);
        r.setConnection(c);
        robotRepository.save(r);
    }
    @Override
    public void  updateRobot( Robot r,  Robot ru) {
        //r.setConnection(ru.getConnection());
        r.setStatusRobot(ru.getStatusRobot());
        r.setModeRobot(ru.getModeRobot());
        r.setOperationStatus(ru.getOperationStatus());
        r.setLevelBattery(ru.getLevelBattery());
        r.setSpeed(ru.getSpeed());
        robotRepository.save(r);
    }
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
        Optional<Robot> r = this.robotRepository. findbyClientId(clientId);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by clientId = " + clientId);
        }
        return r.get();
    }
}

