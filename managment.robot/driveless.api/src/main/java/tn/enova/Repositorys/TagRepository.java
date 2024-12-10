package tn.enova.Repositorys;





import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository {/*extends MongoRepository<Tag, String> {
    @Aggregation(pipeline = {
            "{ $group: { _id: null, codes: { $push: '$code' } }}",
            "{ $project: { '_id': 0,'codes': 1 } }",
            "{ $unwind: '$codes' }"
    })

    List<String> findAllCode();*/
}