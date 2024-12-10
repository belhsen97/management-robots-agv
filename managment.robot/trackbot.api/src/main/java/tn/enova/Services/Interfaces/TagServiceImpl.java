package tn.enova.Services.Interfaces;

import tn.enova.Enums.LevelType;
import tn.enova.Exceptions.MethodArgumentNotValidException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Entitys.Tag;
import tn.enova.Models.Entitys.Workstation;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Repositorys.TagRepository;
import tn.enova.Repositorys.WorkstationRepository;
import tn.enova.Services.DrivelessService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service("tag-service")
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final WorkstationRepository workstationRepository;
    private final DrivelessService drivelessService;
    private final NotificationService notificationService;

    @Override
    public List<Tag> selectAll() {
        return this.tagRepository.findAll();
    }

    @Override
    public Tag selectByCode(String code) {
        Optional<Tag> t = this.tagRepository.findbyCode(code);
        if (t.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Tag by code = " + code);
        }
        return t.get();
    }

    @Override
    public Tag selectById(String id) {
        Optional<Tag> t = this.tagRepository.findById(id);
        if (t.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Tag by id = " + id);
        }
        return t.get();
    }

    @Override
    public Tag insert(Tag t) {
        if (this.tagRepository.findbyCode(t.getCode()).isPresent()) {
            throw new MethodArgumentNotValidException("other Tag found equal " + t.getCode());
        }
        Optional<Workstation> w = workstationRepository.findbyName(t.getWorkstationName());
        t.setWorkstationName(w.isPresent() ? w.get().getName() : null);
        t = this.tagRepository.save(t);
        t.setWorkstation(w.isPresent() ? w.get() : null);

        CompletableFuture<?> insertTagDriveless = this.drivelessService.insertTag(t);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("add new Tag where is code = "+t.getCode()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("add new Tag where is code = "+t.getCode()).build());
        CompletableFuture.allOf(insertTagDriveless, notify1,notify2);
        return t;
    }

    @Override
    public Tag update(String id, Tag obj) {
        Tag t = this.selectById(id);
        t.setWorkstation(null);
        t.setCode(obj.getCode());
        Optional<Workstation> w = workstationRepository.findbyName(obj.getWorkstationName());
        t.setWorkstationName(w.isPresent() ? w.get().getName() : t.getWorkstationName());
        System.out.println(t.getWorkstation());
        t = this.tagRepository.save(t);
        t.setWorkstation(w.get());

        CompletableFuture<?> updateTagDriveless =  this.drivelessService.updateTag(id, t);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("update Tag where is code = "+t.getCode()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("update Tag where is code = "+t.getCode()).build());
        CompletableFuture.allOf(updateTagDriveless, notify1,notify2);
        return t;
    }

    @Override
    public void delete(String id) {
        Tag t = this.selectById(id);
        this.tagRepository.delete(t);

        CompletableFuture<?> deleteTagDriveless = this.drivelessService.deleteTag(id);
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("delete tag where code equal to "+t.getCode()).build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("delete tag where code equal to "+t.getCode()).build());
        CompletableFuture.allOf(deleteTagDriveless, notify1,notify2);
    }


    @Override
    public void deleteAll() {
        this.tagRepository.deleteAll();

        CompletableFuture<?> deleteAllTagsDriveless = this.drivelessService.deleteAllTags();
        CompletableFuture<?> notify1 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.TRACE).message("delete all tags").build());
        CompletableFuture<?> notify2 = this.notificationService.notify(NotificationResponse.builder().level(LevelType.INFO).message("delete all tags").build());
        CompletableFuture.allOf(deleteAllTagsDriveless, notify1,notify2);
    }



}