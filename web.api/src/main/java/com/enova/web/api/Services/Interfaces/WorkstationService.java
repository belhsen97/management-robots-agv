package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Tag;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Exceptions.RessourceNotFoundException;
import com.enova.web.api.Repositorys.RobotRepository;
import com.enova.web.api.Repositorys.TagRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.IWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service("workstation-service")
public class WorkstationService implements IWorkstationService {
    private final WorkstationRepository workstationRepository;
    private final RobotRepository robotRepository;
    private final TagRepository tagRepository;

    @Autowired
    public WorkstationService(WorkstationRepository workstationRepository, RobotRepository robotRepository, TagRepository tagRepository) {
        this.workstationRepository = workstationRepository;
        this.robotRepository = robotRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Workstation> selectAll() {
        return this.workstationRepository.findAll();
    }

    @Override
    public Workstation selectById(String id) {
        Optional<Workstation> w = this.workstationRepository.findById(id);  // return this.workstationRepository.findById(id).orElseThrow();
        if (w.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Workstation by id =" + id);
        }
        return w.get();
    }

    @Override
    public Workstation selectByName(String name) {
        Optional<Workstation> w = this.workstationRepository.findbyName(name);
        if (w.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Workstation by name =" + name);
        }
        return w.get();
    }

    @Override//@Transactional(rollbackFor = MethodArgumentNotValidException.class) // throws UserExistsException {
    public Workstation insert(Workstation obj) {
        if (this.workstationRepository.findbyName(obj.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Workstation found");
        }
        final boolean fullListRobots  = (obj.getRobots() == null && obj.getRobots().isEmpty()? false :true);
        final boolean fullListTags = (obj.getTags() == null && obj.getTags().isEmpty()? false :true);
        if (fullListRobots) {
            obj.getRobots().forEach(robot -> {
                Optional<Robot> robotOptional =   this.robotRepository.findbyName(robot.getName());
                if (robotOptional.isPresent()){robot = robotOptional.get();}
                else{ robot.setCreatedAt(new Date());}
                robot.setWorkstation(null);
                robot.setIdWorkstation(obj.getName());
                this.robotRepository.save(robot);
            });
            //this.robotRepository.saveAll(obj.getRobots());
        }
        if (fullListTags) {
            obj.getTags().forEach(tag -> {
                Optional<Tag> tagOptional =   this.tagRepository.findbyCode(tag.getCode());
                if (tagOptional.isPresent()) {
                    tag = tagOptional.get();
                }
                tag.setWorkstation(null);
                tag.setWorkstationName(obj.getName());
                this.tagRepository.save(tag);
            });
           // this.tagRepository.saveAll(obj.getTags());
        }
        obj.setRobots(null);
        obj.setTags(null);
        final Workstation w = this.workstationRepository.save(obj);
        return this.selectById(w.getId());
    }

    @Override
    @Transactional
    public Workstation update(String id, Workstation obj) {
        Workstation w = this.selectById(id);
        //if (w == null) {return null; }
//        this.robotRepository.changeWorkstation(w.getName(), obj.getName());
//        this.tagRepository.changeWorkstation(w.getName(), obj.getName());



        this.robotRepository.changeWorkstation(w.getName(), null);
        this.tagRepository.changeWorkstation(w.getName(), null);
        final boolean fullListRobots  = (obj.getRobots() == null && obj.getRobots().isEmpty()? false :true);
        final boolean fullListTags = (obj.getTags() == null && obj.getTags().isEmpty()? false :true);
        if (fullListRobots) {
            obj.getRobots().forEach(robot -> {
                Optional<Robot> robotOptional =   this.robotRepository.findbyName(robot.getName());
                if (robotOptional.isPresent()){robot = robotOptional.get();}
                else{ robot.setCreatedAt(new Date());}
                robot.setWorkstation(null);
                robot.setIdWorkstation(obj.getName());
                this.robotRepository.save(robot);
            });
            //this.robotRepository.saveAll(obj.getRobots());
        }
        if (fullListTags) {
            obj.getTags().forEach(tag -> {
                Optional<Tag> tagOptional =   this.tagRepository.findbyCode(tag.getCode());
                if (tagOptional.isPresent()) {
                    tag = tagOptional.get();
                }
                tag.setWorkstation(null);
                tag.setWorkstationName(obj.getName());
                this.tagRepository.save(tag);
            });
            // this.tagRepository.saveAll(obj.getTags());
        }








        w.setName(obj.getName());
        w.setEnable(obj.isEnable());
        w.setRobots(null);
        w.setTags(null);
        w = this.workstationRepository.save(w);
        return this.selectById(w.getId());
    }

    @Override
    public void delete(String id) {
        Workstation w = this.selectById(id);
        this.robotRepository.changeWorkstation(w.getName(), null);
        this.tagRepository.changeWorkstation(w.getName(), null);
        this.workstationRepository.delete(w);
    }

    @Override
    public void deleteAll() {
        this.workstationRepository.deleteAll();
    }

    @Override
    public List<Tag> selectAllTags() {
        return null;//return workstationRepository.findAllTags();
    }
}



/*
    @Override//@Transactional(rollbackFor = MethodArgumentNotValidException.class) // throws UserExistsException {
    public Workstation insert(Workstation obj) {
        if (this.workstationRepository.findbyName(obj.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Workstation found");
        }
        final boolean fullListRobots  = (obj.getRobots() == null && obj.getRobots().isEmpty()? false :true);
        final boolean fullListTags = (obj.getTags() != null && !obj.getTags().isEmpty()? false :true);
        if (fullListRobots) {
            obj.getRobots().forEach(robot -> {
                if (this.robotRepository.findbyName(robot.getName()).isPresent()) {
                    throw new MethodArgumentNotValidException("other Robot found equal " + robot.getName());
                }
                robot.setIdWorkstation(obj.getName());
                robot.setCreatedAt(new Date());
            });
        }
        if (fullListTags) {
            obj.getTags().forEach(tag -> {
                if (this.tagRepository.findbyCode(tag.getCode()).isPresent()) {
                    throw new MethodArgumentNotValidException("other Tag found equal " + tag.getCode());
                }
                tag.setWorkstationName(obj.getName());
            });
        }
        if (fullListRobots && fullListTags) {
            this.tagRepository.saveAll(obj.getTags());
            this.robotRepository.saveAll(obj.getRobots()); }
        obj.setRobots(null);
        obj.setTags(null);
        final Workstation w = this.workstationRepository.save(obj);
        return this.selectById(w.getId());
    }*/