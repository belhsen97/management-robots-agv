package com.enova.web.api.Services.Interfaces;


import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Models.Entitys.Trace;
import com.enova.web.api.Models.Entitys.Workstation;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Exceptions.RessourceNotFoundException;
import com.enova.web.api.Repositorys.RobotPropertyRepository;
import com.enova.web.api.Repositorys.RobotRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.RobotService;
import com.enova.web.api.Services.TraceService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;
    private final RobotPropertyRepository robotPropertyRepository;
    private final WorkstationRepository workstationRepository;
    private final TraceService traceService;
    private final MongoTemplate mongoTemplate;
    @Override
    public List<Robot> selectAll() {
        return this.robotRepository.findAll();
    }

    @Override
    public Robot selectById(String id) {
        Optional<Robot> r = this.robotRepository.findById(id);
        if (r.isEmpty()) {throw new RessourceNotFoundException("Cannot found robot by id = " + id);}
        return r.get();
    }


    @Override
    public Robot selectByName(String name) {
        Optional<Robot> r = this.robotRepository.findByName(name);
        if (r.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found robot by name = " + name);
        }
        return r.get();
    }

    @Override
    public List<RobotProperty> selectDataPropertysAllOrByNameOrUnixTimestamps(String name, Long startUnixTimestamp, Long endUnixTimestamp){
        if ( name == null ){return robotPropertyRepository.findAll();}
        if ( startUnixTimestamp == null &&  endUnixTimestamp == null  ){return robotPropertyRepository.findAllByName(name);}
        Date start;
        Date end;
        if (startUnixTimestamp == null ){
            end = new Date(endUnixTimestamp);
            return robotPropertyRepository.findByNameAndTimestamp( name,  end);
        }
        if (endUnixTimestamp == null ){
            start = new Date(startUnixTimestamp);
            return robotPropertyRepository.findByNameAndTimestamp( name,  start);
        }
         start = new Date(startUnixTimestamp);
         end = new Date(endUnixTimestamp);
        return robotPropertyRepository.findByNameAndTimestampBetween( name,  start,  end);
    }
    @Override
    public Robot insert(Robot obj) {
        if (robotRepository.findByName(obj.getName()).isPresent()) {
            throw new MethodArgumentNotValidException("other Robot found equal " + obj.getName());
        }
        obj.setCreatedAt(new Date());
        Optional<Workstation> w = workstationRepository.findbyName(obj.getNameWorkstation());
        obj.setNameWorkstation(w.isPresent() ? w.get().getName() : null);
        obj = robotRepository.save(obj);
        obj.setWorkstation(w.get());
        traceService.insert(Trace.builder().className("RobotService").methodName("insert").description("add new robot where is name = "+obj.getName()).build());
        return obj;
    }

    @Override
    public Robot update(String id, Robot obj) {
        Robot r = this.selectById(id);
        Optional<Workstation> w = workstationRepository.findbyName(obj.getNameWorkstation());
        r.setWorkstation(null);
        r.setName(obj.getName());
        r.setNameWorkstation(w.isPresent() ? w.get().getName() : r.getNameWorkstation());
        r.setNotice(obj.getNotice());
        r.setStatusRobot(obj.getStatusRobot());
        r.setModeRobot(obj.getModeRobot());
        r.setOperationStatus(obj.getOperationStatus());
        if ((obj.getName() != null ? !obj.getName().equals(r.getName()) : false)){
            r.setName(obj.getName());
            this.updateMultipleNameRobotPropertys (r.getName(),obj.getName());
        }
        if (obj.getClientid() != null){r.setClientid(obj.getClientid());}
        if (obj.getUsername() != null){r.setUsername(obj.getUsername());}
        if (obj.getPassword() != null){r.setPassword(obj.getPassword());}
        r = robotRepository.save(r);
        r.setWorkstation(w.get());
        traceService.insert(Trace.builder().className("RobotService").methodName("update").description("update robot where is name = "+r.getName()).build());
        return r;
    }


    @Override
    public void delete(String id) {
        Robot r = this.selectById(id);
        this.deleteMultipleRobotPropertysByName ( r.getName() );
        robotRepository.delete(r);
        traceService.insert(Trace.builder().className("RobotService").methodName("delete").description("delete robot where is name = "+r.getName()).build());
    }

    @Override
    public void deleteAll() {
        this.robotRepository.deleteAll();
        traceService.insert(Trace.builder().className("RobotService").methodName("deleteAll").description("delete all robot").build());
    }

    private void deleteMultipleRobotPropertysByName ( String name ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        DeleteResult deleteResult = mongoTemplate.remove(query, RobotProperty.class);
        if (deleteResult.getDeletedCount() == 0) {
            throw new MethodArgumentNotValidException("Cannot delete mutiple property of robot may be is empty or name not correct : " + name);
        }
    }
    private void updateMultipleNameRobotPropertys ( String oldName  , String newName ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(oldName));
        Update update = new Update();
        update.set("name", newName);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, RobotProperty.class);
        if (updateResult.getModifiedCount() == 0) {
            throw new MethodArgumentNotValidException("Cannot update mutiple name property of robot may be is empty or old name not correct or the same new name");
        }
    }
}