package com.enova.web.api.Repositorys;

import com.enova.web.api.Entitys.Robot;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends MongoRepository<Robot, String> {
    //[ { { from: "workstation",localField: "idWorkstation", foreignField: "name",as: "workstation",},},
    // {  { workstation: {$arrayElemAt: ["$workstation", 0],}, }, },{ $project: {      "idWorkstation": 0   }  }]
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'workstation', localField: 'idWorkstation', foreignField: 'name', as: 'workstation' } }",
            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }",
            "{ $project: { 'idWorkstation': 0 } }"
    })
    List<Robot> findAll();

    //[ {$match: { "_id": ObjectId("65eae5122991ca2ee794d601") } },
    //    { $lookup: {   from: 'workstation', localField: 'idWorkstation', foreignField: 'name',   as: 'workstation'  } },
    //    { $addFields: {    "workstation": { $arrayElemAt: ["$workstation", 0] }  } }]
    @Aggregation(pipeline = {
            "{ $match: { '_id': ObjectId(?0) } }",
            "{ $lookup: { from: 'workstation', localField: 'idWorkstation', foreignField: 'name', as: 'workstation' } }",
            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }"
    })
    Optional<Robot> findById(String id);

    @Query(value = "{ 'idWorkstation' : ?0 }")
    List<Robot> findAllByIdWorkstation(String idWorstation);




    @Query(value = "{ 'name' : ?0 }")
    Optional<Robot> findbyName(String name);



    default void changeWorkstation (String idW ,String newIdW) {
        List<Robot> rbs =    this.findAllByIdWorkstation(idW);
        if(this.findAllByIdWorkstation(idW).isEmpty()){   return ;  }
        rbs.forEach(robot -> robot.setIdWorkstation(newIdW));
        this.saveAll(rbs);
    }
}