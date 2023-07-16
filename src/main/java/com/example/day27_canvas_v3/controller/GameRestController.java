package com.example.day27_canvas_v3.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.day27_canvas_v3.repository.GameRepository;
import com.example.day27_canvas_v3.service.GameService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/review")
public class GameRestController {
    
    @Autowired 
    GameService gameSvc;

    @Autowired
    GameRepository gameRepo;

    @PutMapping(path="/{cId}" , consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateComment (@PathVariable String cId, @RequestBody Map<String,String> requestBody) {
        if(gameSvc.updateComment(requestBody, cId)) {
            JsonObject jsonResponse =
                Json.createObjectBuilder()
                    .add("comment", requestBody.get("comments"))
                    .add("rating", requestBody.get("rating").toString())
                    .add("timestamp", LocalDate.now().toString())
                    .build();

            return ResponseEntity.ok().body(jsonResponse.toString());
        }

        return ResponseEntity.badRequest().body("Bad Request");
    }

    @GetMapping(path="/{cId}", consumes= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> getLatestComment(@PathVariable String cId){
        Document doc = gameRepo.findLatest(cId);
        Document docList = doc.getList("edited", Document.class).get(0);

        List<Document> game = gameSvc.getGameById(doc.getInteger("gid"));
        JsonObject jsonResposne =
            Json.createObjectBuilder()
                .add("user", doc.getString("user"))
                .add("rating", doc.getInteger("rating"))
                .add("comment", docList.get("comment", Document.class).getString("value"))
                .add("ID", doc.getInteger("gid"))
                .add("posted", doc.getString("posted"))
                .add("name", game.get(0).toJson())
                .add("edited", gameSvc.isUpdate())
                .add("timestamp", LocalDate.now().toString())
                .build();
        return ResponseEntity.ok().body(jsonResposne.toString());
        // return ResponseEntity.badRequest().body("Bad Request");

    }


}
