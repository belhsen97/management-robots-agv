package tn.enova.Repositorys;


import tn.enova.Models.Entitys.RobotProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RobotPropertyRepository extends MongoRepository<RobotProperty, String> {
   @Query(value = "{ 'name' : ?0 }")
   List<RobotProperty> findAllByName(String name);

   List<RobotProperty> findByTimestampBetween(Date startDate, Date endDate);

   List<RobotProperty> findByNameAndTimestamp(String name, Date date);
   List<RobotProperty> findByNameAndTimestampBetween(String name, Date startDate, Date endDate);

}
