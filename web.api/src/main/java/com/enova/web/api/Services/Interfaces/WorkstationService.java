package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Repositorys.RobotRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.IWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return this.workstationRepository.findById(id).orElse(null);
    }

    @Override
    public Workstation insert(Workstation obj) {
        if (workstationRepository.findbyName(obj.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Workstation found");
        }
        return workstationRepository.save(obj);
    }
    @Override
    public Workstation update(String id, Workstation obj) {
        Workstation w = this.selectById(id);
        if (w != null) {return null; }


        if ( !w.getRobots().isEmpty() ) {

           // this.robotRepository.   not finish
        }


        w.setName(obj.getName());
        w.setEnable(obj.isEnable());
        return workstationRepository.save(w);
    }
 //Set<Tag> tags ;
 //Set<Robot> robots ;
    @Override
    public boolean delete(String id) {
        Workstation w = this.selectById(id);
        if (w != null) {
            workstationRepository.delete(w);
            return true;
        }
        return false;
    }
    @Override
    public void deleteAll() {
        this.workstationRepository.deleteAll();
    }
}