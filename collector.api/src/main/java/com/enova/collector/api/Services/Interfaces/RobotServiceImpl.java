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

    @Override
    public void insertDataPropertys( Robot robot) {
        final List<RobotProperty> listPropertys = RobotMapper.convertToRobotPropertyList(robot);

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
    public Robot selectByName( String name) {
        Optional<Robot> r = this.robotRepository.findbyName(name);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by id = " + name);
        }
        return r.get();
    }
}

