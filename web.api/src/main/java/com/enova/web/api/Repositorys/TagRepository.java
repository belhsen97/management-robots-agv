package com.enova.web.api.Repositorys;




import com.enova.web.api.Entitys.Tag;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

    @Aggregation(pipeline = {
            "{ $lookup: { from: 'workstation', localField: 'workstationName', foreignField: 'name', as: 'workstation' } }",
            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }",
            "{ $project: { 'workstationName': 0 } }"
    })
    List<Tag> findAll();

    //[ {$match: { "_id": ObjectId("65eae5122991ca2ee794d601") } },
    //    { $lookup: {   from: 'workstation', localField: 'workstationName', foreignField: 'name',   as: 'workstation'  } },
    //    { $addFields: {    "workstation": { $arrayElemAt: ["$workstation", 0] }  } }]
    @Aggregation(pipeline = {
            "{ $match: { '_id': ObjectId(?0) } }",
            "{ $lookup: { from: 'workstation', localField: 'workstationName', foreignField: 'name', as: 'workstation' } }",
            "{ $addFields: { 'workstation': { $arrayElemAt: ['$workstation', 0] } } }"
    })
    Optional<Tag> findById(String id);

    @Query(value = "{ 'code' : ?0 }")
    Optional<Tag> findbyCode(String code);









    @Query(value = "{ 'workstationName' : ?0 }")
    List<Tag> findAllByNameWorkstation(String nameWorkstation);




    default void changeWorkstation (String oldName ,String newName) {
        List<Tag> ts =    this.findAllByNameWorkstation(oldName);
        if(ts.isEmpty()){   return ;  }
        ts.forEach(t -> t.setWorkstationName(newName));
        this.saveAll(ts);
    }





}