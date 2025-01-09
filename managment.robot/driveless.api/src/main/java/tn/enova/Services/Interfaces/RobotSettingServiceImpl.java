package tn.enova.Services.Interfaces;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tn.enova.Enums.Constraint;
import tn.enova.Enums.TypeProperty;
import tn.enova.Exceptions.RessourceFoundException;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Commons.RobotSetting;
import tn.enova.Services.RobotService;
import tn.enova.Services.RobotSettingService;
import tn.enova.States.GlobalState;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("robot-setting-service")
@RequiredArgsConstructor
public class RobotSettingServiceImpl implements RobotSettingService {

    @Override
    public List<RobotSetting> selectAll() {
        return GlobalState.listRobotSettings;
    }
    @Override
    public Double selectSpeedAverage() {
        Optional<RobotSetting> speedExact =   this.findRobotSettingByCategoryAndConstraint(TypeProperty.SPEED, Constraint.EXACT);
        if(speedExact.isPresent()){ return Double.valueOf(speedExact.get().getValue());}
        Optional<RobotSetting> speedMax =   this.findRobotSettingByCategoryAndConstraint(TypeProperty.SPEED, Constraint.MAX);
        Optional<RobotSetting> speedMin =   this.findRobotSettingByCategoryAndConstraint(TypeProperty.SPEED, Constraint.MIN);
        if(speedMax.isPresent()&&speedMin.isEmpty()){ return Double.valueOf(speedMax.get().getValue());}
        if(speedMax.isEmpty()&&speedMin.isPresent()){ return Double.valueOf(speedMin.get().getValue());}
        return (Double.valueOf(speedMax.get().getValue())+Double.valueOf(speedMin.get().getValue()))/2;
    }

    private Optional<RobotSetting> findById(String id) {
        return GlobalState.listRobotSettings.stream()
                .filter(tag -> tag.getId().equals(id))
                .findFirst();
    }

    @Override
    public RobotSetting selectById(String id) {
        Optional<RobotSetting> opt = this.findById(id);
        if (opt.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found Robot Config by id = " + id);
        }
        return opt.get();
    }

    @Override
    public List<RobotSetting> selectByTypeProperty(TypeProperty t) {
        return  GlobalState.listRobotSettings.stream().filter(r -> r.getCategory().equals(t)).collect(Collectors.toList());
    }


    @Override
    public RobotSetting insert(RobotSetting object) {
        Optional<RobotSetting> rs = this.findById(object.getId());
        if (rs.isPresent()) {
            throw new RessourceFoundException("found RobotSetting by id = " + object.getId());
        }
        GlobalState.listRobotSettings.add(object);
        return object;
    }

    @Override
    public void insert(List<RobotSetting> robotSettingList) {
        for (RobotSetting robotSetting : robotSettingList) {
            this.insert(robotSetting);
        }
    }

    @Override
    public RobotSetting update(String id, RobotSetting object) {
        RobotSetting existingRobotSetting = selectById(id);
        existingRobotSetting.setValue(object.getValue());
        return existingRobotSetting;
    }
    private Optional<RobotSetting> findRobotSettingByCategoryAndConstraint(TypeProperty category, Constraint constraint) {
        return GlobalState.listRobotSettings.stream()
                .filter(rs -> rs.getCategory().equals(category) && rs.getConstraint().equals(constraint))
                .findFirst();
    }
    @Override
    public List<RobotSetting> update(List<RobotSetting> objects) {
        for (int i = 0; i < objects.size(); i++) {
            final Optional<RobotSetting> opt = this.findRobotSettingByCategoryAndConstraint(objects.get(i).getCategory(), objects.get(i).getConstraint());
            if (opt.isEmpty()) {
                throw new RessourceNotFoundException("Cannot found Robot Config by category =" + objects.get(i).getCategory().name() + " and by constraint" + objects.get(i).getConstraint());
            }
            opt.get().setValue(objects.get(i).getValue());
        }
        return objects;
    }

    @Override
    public void delete(String id) {
        boolean removed = GlobalState.listRobotSettings.removeIf(robotSetting -> robotSetting.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("RobotSetting not found with id: " + id);
        }
    }

    @Override
    public void deleteAll() {
        GlobalState.listRobotSettings.clear();
    }
}
