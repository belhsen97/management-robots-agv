package tn.enova.Services.Interfaces;


import tn.enova.Enums.LevelType;
import tn.enova.Exceptions.MethodArgumentNotValidException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Entitys.RobotSetting;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Repositorys.RobotSettingRepository;
import tn.enova.Services.DrivelessService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.RobotSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service("robot-setting-service")
@RequiredArgsConstructor
public class RobotSettingServiceImpl implements RobotSettingService {
    private final RobotSettingRepository repository;
    private final DrivelessService drivelessService;
    private final NotificationService notificationService;

    @Override
    public List<RobotSetting> selectAll() {
        return this.repository.findAll();
    }

    @Override
    public RobotSetting selectById(String id) {
        Optional<RobotSetting> opt = this.repository.findById(id);
        if (opt.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Robot Config by id = " + id);
        }
        return opt.get();
    }

    @Override
    public RobotSetting insert(RobotSetting object) {
        final Optional<RobotSetting> opt = this.repository.findRobotSettingByCategoryAndConstraint(object.getCategory(), object.getConstraint());
        if (opt.isPresent()) {
            throw new MethodArgumentNotValidException("other Robot Config found equal " + object.getCategory().name() + " and " + object.getConstraint().name());
        }
        object = repository.save(object);
        this.drivelessService.insertRobotSetting( object);

        return object;
    }

    @Override
    public RobotSetting update(String id, RobotSetting object) {
        RobotSetting r = this.selectById(id);
        r.setValue(object.getValue());
        this.drivelessService.updateRobotSetting( id,  object);
        return repository.save(object);
    }

    @Override
    public List<RobotSetting> update(List<RobotSetting> objects) {
        for (int i = 0; i < objects.size(); i++) {
            final Optional<RobotSetting> opt = this.repository.findRobotSettingByCategoryAndConstraint(objects.get(i).getCategory(), objects.get(i).getConstraint());
            if (opt.isEmpty()) {
                throw new RessourceNotFoundException("Cannot found Robot Config by category =" + objects.get(i).getCategory().name() + " and by constraint" + objects.get(i).getConstraint());
            }
            opt.get().setValue(objects.get(i).getValue());
            objects.set(i, repository.save(opt.get()));
        }
        this.drivelessService.updateMultiRobotSetting(objects);
        return objects;
    }

    @Override
    public void delete(String id) {
        RobotSetting r = this.selectById(id);
        repository.delete(r);
        this.drivelessService.deleteRobotSetting(id);
        //traceService.insert(Trace.builder().className("Robot Config Service").methodName("delete").description("delete robot Config where is Category = " + r.getCategory() + " and Constraint" + r.getConstraint()).build());
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
        this.drivelessService.deleteAllRobotSettings();
        //traceService.insert(Trace.builder().className("Robot Config Service").methodName("deleteAll").description("delete all robot Category").build());
    }
}
