package tn.enova.Repositorys;

import tn.enova.Models.Entitys.Robot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotRepository extends MongoRepository<Robot, String> {
    //[ { { from: "workstation",localField: "nameWorkstation", foreignField: "name",as: "workstation",},},
    // {  { workstation: {$arrayElemAt: ["$workstation", 0],}, }, },{ $project: {      "nameWorkstation": 0   }  }]
//    @Aggregation(pipeline = {
//            "{ $lookup: { from: 'workstation', localField: 'nameWorkstation', foreignField: 'name', as: 'workstation' } }",
//            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }",
//            "{ $project: { 'nameWorkstation': 0 } }"
//    })
//    List<Robot> findAll();

    //[ {$match: { "_id": ObjectId("65eae5122991ca2ee794d601") } },
    //    { $lookup: {   from: 'workstation', localField: 'nameWorkstation', foreignField: 'name',   as: 'workstation'  } },
    //    { $addFields: {    "workstation": { $arrayElemAt: ["$workstation", 0] }  } }]
//    @Aggregation(pipeline = {
//            "{ $match: { '_id': ObjectId(?0) } }",
//            "{ $lookup: { from: 'workstation', localField: 'nameWorkstation', foreignField: 'name', as: 'workstation' } }",
//            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }"
//    })
//    Optional<Robot> findById(String id);

//    @Query(value = "{ 'nameWorkstation' : ?0 }")
//    List<Robot> findAllByNameWorkstation(String nameWorstation);



    //@Query(value = "{ 'name' : ?0 }")
//    @Aggregation(pipeline = {
//            "{ $match: { 'name': ?0 } }",
//            "{ $lookup: { from: 'workstation', localField: 'nameWorkstation', foreignField: 'name', as: 'workstation' } }",
//            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }"
//    })
    Optional<Robot> findByName(String name);

    Optional<Robot> findRobotByCodeTag(String code);



//    default void changeWorkstation (String oldName ,String newName) {
//        List<Robot> rbs =    this.findAllByNameWorkstation(oldName);
//        if(rbs.isEmpty()){   return ;  }
//        rbs.forEach(robot -> robot.setNameWorkstation(newName));
//        this.saveAll(rbs);
//    }
}