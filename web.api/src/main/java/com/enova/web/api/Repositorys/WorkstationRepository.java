package com.enova.web.api.Repositorys;

import com.enova.web.api.Models.Entitys.Workstation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkstationRepository extends MongoRepository<Workstation, String> {
//    [{  $lookup:  { from: "robot", localField: "name", foreignField: "idWorkstation", as: "robots"}},
//    {  $lookup: {  from: 'tag', localField: 'name', foreignField: 'workstationName', as: 'tags' } }]
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'robot', localField: 'name', foreignField: 'idWorkstation', as: 'robots' } }",
            "{ $lookup: {  from: 'tag', localField: 'name', foreignField: 'workstationName', as: 'tags' } }"
    })
    List<Workstation> findAll();
    @Aggregation(pipeline = {
            "{ $match: { '_id': ObjectId(?0) } }",
            "{ $lookup: { from: 'robot', localField: 'name', foreignField: 'idWorkstation', as: 'robots' } }",
            "{ $lookup: {  from: 'tag', localField: 'name', foreignField: 'workstationName', as: 'tags' } }"
    })
    Optional<Workstation> findById(String id);

    @Query(value = "{ 'name' : ?0 }")
    Optional<Workstation> findbyName(String name);



//[   { $project: { tags: "$tags"  }  },  { $unwind: "$tags" }, {  $replaceRoot: { newRoot: "$tags" } }]

//    @Aggregation(pipeline = {
//            "{ $project: { tags: '$tags'}}",
//            "{ $unwind: '$tags'}",
//            "{ $replaceRoot: { newRoot: '$tags' }}"
//    })
//    List<Tag> findAllTags();

}