package com.enova.collector.api.Repositorys;


import com.enova.collector.api.Models.Entitys.RobotProperty;
import com.enova.collector.api.Enums.TypeProperty;
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
