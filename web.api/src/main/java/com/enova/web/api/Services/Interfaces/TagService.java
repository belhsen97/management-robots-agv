package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Tag;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Exceptions.RessourceNotFoundException;
import com.enova.web.api.Repositorys.TagRepository;
import com.enova.web.api.Repositorys.WorkstationRepository;
import com.enova.web.api.Services.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("tag-service")
public class TagService implements ITagService {
    private final TagRepository tagRepository;
    private final WorkstationRepository workstationRepository;

    @Autowired
    public TagService(TagRepository tagRepository, WorkstationRepository workstationRepository) {
        this.tagRepository = tagRepository;
        this.workstationRepository = workstationRepository;
    }

    @Override
    public List<Tag> selectAll() {
        return tagRepository.findAll();
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
        if (tagRepository.findbyCode(t.getCode()).isPresent()) {
            throw new MethodArgumentNotValidException("other Tag found equal "+t.getCode());
        }
        Optional<Workstation> w = workstationRepository.findbyName(t.getWorkstationName());
        t.setWorkstationName(w.isPresent() ? w.get().getName() : null);
        t = tagRepository.save(t);
        t.setWorkstation(w.get());
        return t;
    }

    @Override
    public Tag update(String id, Tag obj) {
        Tag t = this.selectById(id);
        t.setWorkstation(null);
        t.setCode(obj.getCode());
        t.setDescription(obj.getDescription());
        Optional<Workstation> w = workstationRepository.findbyName(obj.getWorkstationName());
        t.setWorkstationName(w.isPresent() ? w.get().getName() : t.getWorkstationName());
        System.out.println(t.getWorkstation());
        t = tagRepository.save(t);
        t.setWorkstation(w.get());
        return t;
    }

    @Override
    public void delete(String id) {
        Tag t = this.selectById(id);
        tagRepository.delete(t);
    }


    @Override
    public void deleteAll() {
        this.tagRepository.deleteAll();
    }


//    @Override
//    public Tag insert(Tag object) {
//        Optional<Workstation> w =  this.workstationRepository.findbyName(object.getWorkstationName());
//        if ( w.isEmpty()){
//            throw new RessourceNotFoundException("Cannot found Workstation by name ="+object.getWorkstationName() );
//        }
//        object.setId(w.get().getLengthTags()+1);
//        w.get().addTag(object);
//        w.get().setRobots(null);
//        this.workstationRepository.save( w.get());
//        return object;
//    }

}