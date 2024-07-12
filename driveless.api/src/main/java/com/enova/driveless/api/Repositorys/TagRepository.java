package com.enova.driveless.api.Repositorys;




import com.enova.driveless.api.Models.Entitys.Tag;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    @Aggregation(pipeline = {
            "{ $group: { _id: null, codes: { $push: '$code' } }}",
            "{ $project: { '_id': 0,'codes': 1 } }",
            "{ $unwind: '$codes' }"
    })

    List<String> findAllCode();
}