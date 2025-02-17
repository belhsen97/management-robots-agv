package tn.enova.Services.Interfaces;

import tn.enova.Enums.LevelType;
import tn.enova.Exceptions.MethodArgumentNotValidException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Entitys.*;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Repositorys.TagRepository;
import tn.enova.Repositorys.WorkstationRepository;
import tn.enova.Services.NotificationService;
import tn.enova.Services.WorkstationService;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service("workstation-service")
@RequiredArgsConstructor
public class WorkstationServiceImpl implements WorkstationService {

    private final WorkstationRepository workstationRepository;
    private final TagRepository tagRepository;
    private final MongoTemplate mongoTemplate;
    private final NotificationService notificationService;

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
        if (this.workstationRepository.findbyName(obj.getName()).isPresent()) {    throw new MethodArgumentNotValidException("other Workstation found");   }

        final boolean fullListTags = (obj.getTags() == null && obj.getTags().isEmpty() ? false : true);
        if (fullListTags) {
            obj.getTags().forEach(tag -> {
                Optional<Tag> tagOptional = this.tagRepository.findbyCode(tag.getCode());
                if (tagOptional.isPresent()) {
                    tag = tagOptional.get();
                }
                tag.setWorkstation(null);
                tag.setWorkstationName(obj.getName());
                this.tagRepository.save(tag);
            });
            // this.tagRepository.saveAll(obj.getTags());
        }
        obj.setTags(null);
        Workstation w = this.workstationRepository.save(obj);
        w = this.selectById(w.getId());
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("add new Workstation where is name = " + obj.getName()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("add new Workstation where is name = " + obj.getName()).build());
        CompletableFuture.allOf(notify1, notify2);
        return w;
    }

    @Override
    @Transactional
    public Workstation update(String id, Workstation obj) {
        Workstation w = this.selectById(id);
        this.updateMultipleWorstationNameOfTags(w.getName(), null);
        final boolean fullListTags = (obj.getTags() == null && obj.getTags().isEmpty() ? false : true);
        if (fullListTags) {
            obj.getTags().forEach(tag -> {
                Optional<Tag> tagOptional = this.tagRepository.findbyCode(tag.getCode());
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
        w.setTags(null);
        w = this.workstationRepository.save(w);
        w = this.selectById(w.getId());
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("update Workstation where is name = " + w.getName()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("update Workstation where is name = " + w.getName()).build());
        CompletableFuture.allOf(notify1, notify2);
        return w;
    }

    @Override
    public void delete(String id) {
        Workstation w = this.selectById(id);
        this.updateMultipleWorstationNameOfTags(w.getName(), null);
        this.workstationRepository.delete(w);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("delete Workstation where is name = " + w.getName()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("delete Workstation where is name = " + w.getName()).build());
        CompletableFuture.allOf(notify1, notify2);
    }

    @Override
    public void deleteAll() {
        this.workstationRepository.deleteAll();
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("delete all Workstation").build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("delete all Workstation").build());
        CompletableFuture.allOf(notify1, notify2);
    }

    //    private void updateMultipleWorstationNameOfRobots( String oldName  , String newName ) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("nameWorkstation").is(oldName));
//        Update update = new Update();
//        update.set("nameWorkstation", newName);
//        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Robot.class);
//        if (updateResult.getModifiedCount() == 0) {
//            throw new MethodArgumentNotValidException("Cannot update mutiple name workstation of robot may be is empty or old name not correct or the same new name");
//        }
//    }
    private void updateMultipleWorstationNameOfTags(String oldName, String newName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("workstationName").is(oldName));
        Update update = new Update();
        update.set("workstationName", newName);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Tag.class);
        if (updateResult.getModifiedCount() == 0) {
            throw new MethodArgumentNotValidException("Cannot update mutiple name workstation of tag may be is empty or old name not correct or the same new name");
        }
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