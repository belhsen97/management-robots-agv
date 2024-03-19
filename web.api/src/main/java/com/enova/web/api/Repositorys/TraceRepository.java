package com.enova.web.api.Repositorys;


import com.enova.web.api.Entitys.Trace;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraceRepository extends MongoRepository<Trace, String> {
    //    [{$lookup:{ from: 'user', localField: 'username', foreignField: 'username',   as: 'user'  } },
//    {$addFields: {    'user': { $arrayElemAt: ['$user', 0] }  }},
//    { $project: { 'username': 0 } }]



    @Aggregation(pipeline = {
            "{ $lookup: { from: 'user', localField: 'username', foreignField: 'username', as: 'user' } }",
            "{ $addFields: { 'user': { $arrayElemAt: ['$user', 0] } } }",
            //"{ $project: { 'username': 0 } }"
    })
    List<Trace> findAll();
    //[ {$match: { "_id": ObjectId("65eae5122991ca2ee794d601") } },
    //    { $lookup: {   from: 'user', localField: 'username', foreignField: 'name',   as: 'user'  } },
    //    { $addFields: {    "user": { $arrayElemAt: ["user", 0] }  } }]
    @Aggregation(pipeline = {
            "{ $match: { '_id': ObjectId(?0) } }",
            "{ $lookup: { from: 'user', localField: 'username', foreignField: 'username', as: 'user' } }",
            "{ $addFields: { 'user': { $arrayElemAt: ['$user', 0] } } }"
    })
    Optional<Trace> findById(String id);
}
