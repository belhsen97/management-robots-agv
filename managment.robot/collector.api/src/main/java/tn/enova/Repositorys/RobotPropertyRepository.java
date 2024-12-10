package tn.enova.Repositorys;


import tn.enova.Enums.TypeProperty;
import tn.enova.Models.Entitys.RobotProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotPropertyRepository extends MongoRepository<RobotProperty, String> {
   // long countByType(TypeProperty type);
   // @Query(value = "{ 'type': ?1, 'value': ?0 }", sort = "{ 'timestamp' : -1 }", limit = 1)
   // Optional<RobotProperty> findLatestByValueAndType(String value, TypeProperty type);
   Optional<RobotProperty> findFirstByTypeOrderByTimestampDesc( TypeProperty type);
}
