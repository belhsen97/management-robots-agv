package com.enova.web.api.Services.Interfaces;


import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Repositorys.RobotRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.IRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("robot-service")
public class RobotService implements IRobotService {

    private final RobotRepository robotRepository;
    private final WorkstationRepository workstationRepository;

    @Autowired
    public RobotService(RobotRepository robotRepository,
                        WorkstationRepository workstationRepository) {
        this.robotRepository = robotRepository;
        this.workstationRepository = workstationRepository;
    }

    @Override
    public List<Robot> selectAll() {
        return this.robotRepository.findAll();
    }

    @Override
    public Robot selectById(String id) {
        return this.robotRepository.findById(id).orElse(null);
    }

    @Override
    public Robot insert(Robot obj) {
        if (robotRepository.findbyName(obj.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Robot found");
        }
        obj.setCreatedAt(new Date());
        Optional<Workstation> w = workstationRepository.findbyName(obj.getName());
        obj.setIdWorkstation(w.isPresent() ? w.get().getName() : null);
        return robotRepository.save(obj);
    }

    @Override
    public Robot update(String id, Robot obj) {
        Robot r = this.selectById(id);
        if (r != null) {
            r.setName(obj.getName());

            Optional<Workstation> w = workstationRepository.findbyName(obj.getName());
            r.setIdWorkstation(w.isPresent() ? w.get().getName() : r.getIdWorkstation());

            r.setNotice(obj.getNotice());
            //remove it cause reel time !!!!!!!!!!!!!
            r.setConnection(obj.getConnection());
            r.setStatusRobot(obj.getStatusRobot());
            r.setModeRobot(obj.getModeRobot());
            r.setOperationStatus(obj.getOperationStatus());
            r.setLevelBattery(obj.getLevelBattery());
            r.setSpeed(obj.getSpeed());

            return robotRepository.save(r);
        }
        return null;
    }


    @Override
    public boolean delete(String id) {
        Optional<Robot> r = this.robotRepository.findById(id);
        if (r.isPresent()) {
            robotRepository.delete(r.get());
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        this.robotRepository.deleteAll();
    }
}