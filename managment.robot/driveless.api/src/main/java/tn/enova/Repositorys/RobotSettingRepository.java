package tn.enova.Repositorys;

import tn.enova.Enums.Constraint;
import tn.enova.Enums.TypeProperty;
import tn.enova.Models.Commons.RobotSetting;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotSettingRepository { /*extends MongoRepository<RobotSetting, String> {
    @Query(value = "{ 'category' : ?0 ,'constraint' : ?1 }")
    Optional<RobotSetting> findRobotSettingByCategoryAndConstraint(TypeProperty category, Constraint constraint);*/
}