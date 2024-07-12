package com.enova.driveless.api.Services.Interfaces;

import com.enova.driveless.api.Repositorys.TagRepository;
import com.enova.driveless.api.Services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tag-service")
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<String> selectAllCode() {
        return tagRepository.findAllCode();
    }
}