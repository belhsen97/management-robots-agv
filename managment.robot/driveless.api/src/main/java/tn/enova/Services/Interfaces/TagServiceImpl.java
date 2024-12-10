package tn.enova.Services.Interfaces;


import tn.enova.Exceptions.RessourceFoundException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Commons.Tag;
import tn.enova.Services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enova.States.GlobalState;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("tag-service")
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    @Override
    public List<Tag> selectAll() {
        return GlobalState.listTags;
    }
    private Optional<Tag> findByCode(String code) {
        return GlobalState.listTags.stream()
                .filter(tag -> tag.getCode().equals(code))
                .findFirst();
    }
    private Optional<Tag> findById(String id) {
        return GlobalState.listTags.stream()
                .filter(tag -> tag.getId().equals(id))
                .findFirst();
    }
    @Override
    public Tag selectById(String id) {
        Optional<Tag> t = this.findById(id);
        if (t.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Tag by id = " + id);
        }
        return t.get();
    }
    @Override
    public Tag selectByCode(String code) {
        Optional<Tag> t = this.findByCode(code);
        if (t.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Tag by code = " + code);
        }
        return t.get();
    }
    /*@Override
    public Optional<Tag> selectByCode(String code) {
        return GlobalState.listTags.stream()
                .filter(tag -> tag.getCode().equals(code))
                .findFirst();
    }*/
    @Override
    public Tag insert(Tag tag) {
        Optional<Tag> t = this.findById(tag.getId());
        if (t.isPresent()) {
            throw new RessourceFoundException("found Tag by id = " + tag.getId());
        }
        GlobalState.listTags.add(tag);
        return tag;
    }
    @Override
    public void insert(List<Tag> tags) {
        for (Tag tag : tags) {
            this.insert(tag);
        }
    }
    @Override
    public Tag update(String id, Tag updatedTag) {
        Tag existingTag = selectById(id);
        existingTag.setCode(updatedTag.getCode());
        existingTag.setWorkstationName(updatedTag.getWorkstationName());
        return existingTag;
    }
    @Override
    public void delete(String id) {
        boolean removed = GlobalState.listTags.removeIf(tag -> tag.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Tag not found with id: " + id);
        }
    }

    @Override
    public void deleteAll() {
        GlobalState.listTags.clear();
    }

    @Override
    public List<String> selectAllCode() {
        return GlobalState.listTags.stream()
                .map(Tag::getCode)
                .collect(Collectors.toList());
    }
}