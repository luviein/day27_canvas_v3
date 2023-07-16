package com.example.day27_canvas_v3.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.result.UpdateResult;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

@Repository
public class GameRepository {

    @Autowired
    MongoTemplate template;

    public Document postComment(MultiValueMap<String, String> form) {
        String cid = UUID.randomUUID().toString().substring(0, 8);
        Document doc = new Document();
        doc.put("cId", cid);
        doc.put("user", form.getFirst("name"));
        doc.put("rating", Integer.parseInt(form.getFirst("rating")));
        doc.put("c_text", form.getFirst("comments"));
        doc.put("gid", Integer.parseInt(form.getFirst("gid")));
        doc.put("posted", LocalDate.now().toString());

        return template.insert(doc, "comment");
    }

    /*
     * db.getCollection("comment").updateOne(
     * {cId: "98fbba70"},
     * {
     * $set: {rating : 2 },
     * $push: {edited: "this is an edited comment v2"}
     * }
     * 
     * )
     */

    public Boolean updateComment(Map<String,String> requestBody , String cId) {
        Query query = Query.query(
            Criteria.where("cId").is(cId)
        );

        JsonObjectBuilder commentObjectBuilder = Json.createObjectBuilder()
            .add("comment", requestBody.get("comments"))
            .add("rating", Integer.parseInt(requestBody.get("rating")))
            .add("posted", LocalDate.now().toString());
        
        JsonArrayBuilder editedArrayBuilder = Json.createArrayBuilder()
            .add(commentObjectBuilder.build());    
        
        Update updateOps = new Update()
            .set("rating", Integer.parseInt(requestBody.get("rating")))
            .set("posted", LocalDate.now().toString())
            .push("edited", editedArrayBuilder.build());
        
        UpdateResult updateResult = template.updateFirst(query, updateOps, Document.class, "comment");
        System.out.println("req body rating >>>>"+requestBody.get("rating"));
        System.out.println("req body rating >>>>"+requestBody.get("comments"));
        return (updateResult.getModifiedCount() > 0 ? true : false);
    }


    public List<Document> getGameById (Integer gameID) {
        Query query = Query.query(
            Criteria.where("gid").is(gameID)
        );
        query.fields()
            .exclude("_id")
            .include("name");
        
        return template.find(query, Document.class, "game");
    } 

    /*
     * db.getCollection("comment").find({
            cId: "c0852aaf"
        }, {user: 1, rating: 1, c_text: 1, gid : 1, posted : 1, _id: 0}
        )
     */

    public List<Document> getLatestReview (String cId) {
        Query query = Query.query(
            Criteria.where("cId").is(cId)
        );
        query.fields()
            .exclude("_id")
            .include("user", "rating", "c_text", "gid", "posted");

        return template.find(query, Document.class, "comment");
    }

//     db.getCollection("comment").aggregate([
// {$match: {cId:"2be4c24c"}},
// {$unwind: "$edited"},
// {$sort: {posted: -1}},
// {$limit: 1},
// { $project: {
//         resp : { $arrayElemAt: ['$edited',0] }
//     }}


// ])

    public Document findLatest(String cId) {
        MatchOperation match = Aggregation.match(Criteria.where("cId").is(cId));
        UnwindOperation unwind = Aggregation.unwind("edited");
        SortOperation sort = Aggregation.sort(Sort.by(org.springframework.data.domain.Sort.Direction.ASC,"posted"));
        LimitOperation limit = Aggregation.limit(1);
        ProjectionOperation project = Aggregation.project("resp");
        Aggregation pipeline = Aggregation.newAggregation(match, unwind, sort, limit);
        return template.aggregate(pipeline, "comment", Document.class).getMappedResults().get(0);
    }

}
