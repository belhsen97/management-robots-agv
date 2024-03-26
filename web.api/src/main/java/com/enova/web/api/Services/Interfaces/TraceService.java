package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Models.Entitys.Trace;
import com.enova.web.api.Exceptions.RessourceNotFoundException;
import com.enova.web.api.Repositorys.TraceRepository;
import com.enova.web.api.Services.IAuthenticationService;
import com.enova.web.api.Services.ITraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("trace-service")
@RequiredArgsConstructor
public class TraceService implements ITraceService {
    private final TraceRepository traceRepository;
    private final IAuthenticationService authService;

    @Override
    public List<Trace> selectAll() {
        return this.traceRepository.findAll();
    }

    @Override
    public Trace selectById(String id) {
        Optional<Trace> t = this.traceRepository.findById(id);
        if (t.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Trace by id = " + id);
        }
        return t.get();
    }

    @Override
    public Trace insert(Trace object) {
        object.setTimestamp(new Date());
        object.setUsername(authService.getUsername());
        return this.traceRepository.save(object);
    }

    @Override
    public Trace update(String id, Trace object) {
        Trace t = this.selectById(id);
        t.setDescription(object.getDescription());
        t.setClassName(object.getClassName());
        t.setMethodName(object.getMethodName());
        return this.traceRepository.save(t);
    }

    @Override
    public void delete(String id) {
        Trace t = this.selectById(id);
        this.traceRepository.delete(t);
    }

    @Override
    public void deleteAll() {
        this.traceRepository.deleteAll();
    }
}
