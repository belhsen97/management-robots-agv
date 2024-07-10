package com.enova.driveless.api.Repositorys;

import com.enova.driveless.api.Enums.Constraint;
import com.enova.driveless.api.Enums.TypeProperty;
import com.enova.driveless.api.Models.Entitys.RobotSetting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotSettingRepository extends MongoRepository<RobotSetting, String> {
    @Query(value = "{ 'category' : ?0 ,'constraint' : ?1 }")
    Optional<RobotSetting> findRobotSettingByCategoryAndConstraint(TypeProperty category, Constraint constraint);
}