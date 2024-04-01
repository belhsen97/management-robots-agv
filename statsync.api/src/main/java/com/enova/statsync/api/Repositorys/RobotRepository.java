package com.enova.statsync.api.Repositorys;



import com.enova.statsync.api.Models.Entitys.Robot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends MongoRepository<Robot, String> {
    @Query(value = "{ 'idWorkstation' : ?0 }")
    List<Robot> findAllByIdWorkstation(String idWorstation);

    @Query(value = "{ 'name' : ?0 }")
    Optional<Robot> findbyName(String name);
}