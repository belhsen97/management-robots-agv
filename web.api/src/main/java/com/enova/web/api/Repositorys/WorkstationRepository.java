package com.enova.web.api.Repositorys;

import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Workstation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkstationRepository extends MongoRepository<Workstation, String> {
    //[{  $lookup:  { from: "robot", localField: "name", foreignField: "idWorkstation", as: "robots",},}]
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'robot', localField: 'name', foreignField: 'idWorkstation', as: 'robots' } }"
    })
    List<Workstation> findAll();
    @Aggregation(pipeline = {
            "{ $match: { '_id': ObjectId(?0) } }",
            "{ $lookup: { from: 'robot', localField: 'name', foreignField: 'idWorkstation', as: 'robots' } }"
    })
    Optional<Workstation> findById(String id);

    @Query(value = "{ 'name' : ?0 }")
    Optional<Workstation> findbyName(String name);
}