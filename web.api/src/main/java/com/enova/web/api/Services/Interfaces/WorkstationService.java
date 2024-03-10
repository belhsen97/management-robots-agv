package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Exceptions.RessourceNotFoundException;
import com.enova.web.api.Repositorys.RobotRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.IWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service("workstation-service")
public class WorkstationService implements IWorkstationService {
    private final WorkstationRepository workstationRepository;
    private final RobotRepository robotRepository;
    @Autowired
    public WorkstationService(WorkstationRepository workstationRepository,RobotRepository robotRepository) {
        this.workstationRepository = workstationRepository;
        this.robotRepository = robotRepository;
    }

    @Override
    public List<Workstation> selectAll() {return this.workstationRepository.findAll();  }

    @Override
    public Workstation selectById(String id) {
        Optional<Workstation> w =  this.workstationRepository.findById(id);
        if ( w.isEmpty()){
            throw new RessourceNotFoundException("Cannot found Workstation by id ="+id );
        }
        return w.get();
       // return this.workstationRepository.findById(id).orElseThrow();
    }

    @Override
    public Workstation insert(Workstation obj) {
        if (workstationRepository.findbyName(obj.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Workstation found");
        }
       if (obj.getRobots() != null && !obj.getRobots().isEmpty()){
           obj.getRobots().forEach(robot -> {
               robot.setIdWorkstation(obj.getName());
               robot.setCreatedAt(new Date());
           });
           robotRepository.saveAll(obj.getRobots());
       }
        obj.setRobots(null);
        return workstationRepository.save(obj);
    }
    @Override
    public Workstation update(String id, Workstation obj) {
        Workstation w = this.selectById(id);
        //if (w == null) {return null; }
        this.robotRepository.changeWorkstation(w.getName(),obj.getName());
        w.setName(obj.getName());
        w.setEnable(obj.isEnable());
        return workstationRepository.save(w);
    }
    @Override
    public boolean delete(String id) {
        Workstation w = this.selectById(id);
        if (w != null) {
            workstationRepository.delete(w);
            robotRepository.changeWorkstation(w.getName(),null);
            return true;
        }
        return false;
    }
    @Override
    public void deleteAll() {
        this.workstationRepository.deleteAll();
    }
}