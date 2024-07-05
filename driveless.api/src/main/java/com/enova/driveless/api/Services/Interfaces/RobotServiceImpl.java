package com.enova.driveless.api.Services.Interfaces;




import com.enova.driveless.api.Enums.Connection;
import com.enova.driveless.api.Exceptions.RessourceNotFoundException;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Repositorys.RobotRepository;
import com.enova.driveless.api.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;

    @Override
    public Optional<Robot> selectByUsernameAndPassword(String username, String password) {
        return robotRepository.findbyUsernameAndPassword( username,  password);
    }

    @Override
    public Robot selectByClientId( String clientId) {
        Optional<Robot> r = this.robotRepository. findbyClientId(clientId);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by clientId = " + clientId);
        }
        return r.get();
    }
    @Override
    public void  updateRobotConnection( String clientId ,  Connection c) {
        Robot r =  this.selectByClientId(clientId);
        r.setConnection(c);
        robotRepository.save(r);
    }

    @Override
    public Robot selectByName(String name) {
        Optional<Robot> r =   robotRepository.findbyName(name);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by name = " + name);
        }
        return r.get();
    }
}

