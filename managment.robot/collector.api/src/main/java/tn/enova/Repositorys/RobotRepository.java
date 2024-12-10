package tn.enova.Repositorys;


import tn.enova.Models.Entitys.Robot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends MongoRepository<Robot, String> {
    @Query(value = "{ 'nameWorkstation' : ?0 }")
    List<Robot> findAllByNameWorkstation(String nameWorstation);

    @Query(value = "{ 'name' : ?0 }")
    Optional<Robot> findbyName(String name);

    @Query(value = "{ 'clientid' : ?0 }")
    Optional<Robot> findbyClientId(String clientId);
}