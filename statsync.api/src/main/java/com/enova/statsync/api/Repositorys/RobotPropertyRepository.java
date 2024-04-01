package com.enova.statsync.api.Repositorys;

import com.enova.statsync.api.Models.Entitys.RobotProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotPropertyRepository extends MongoRepository<RobotProperty, String> {
   @Query(value = "{ 'name' : ?0 }")
   List<RobotProperty> findAllByName(String name);
}
