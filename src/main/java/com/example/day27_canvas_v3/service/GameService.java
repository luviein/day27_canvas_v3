package com.example.day27_canvas_v3.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.example.day27_canvas_v3.repository.GameRepository;

@Service
public class GameService {
    private Boolean update = false;

    
    // public Boolean getUpdate() {
    //     return update;
    // }

    // public void setUpdate(Boolean update) {
    //     this.update = update;
    // }

    @Autowired
    GameRepository gameRepo;

    public Document postComment (MultiValueMap<String,String> form) {
        return gameRepo.postComment(form);
    }

    public Boolean updateComment(Map<String,String> requestBody, String cId) {
        if(gameRepo.updateComment(requestBody, cId)) {
            this.update = true;
        }
        return gameRepo.updateComment(requestBody , cId);
    }

    public Boolean isUpdate(){
        return update;
    }

    // public Boolean updateTrueFalse(Map<String,String> requestBody, String cId) {
    //     if (gameRepo.updateComment(requestBody , cId)){
    //         return true;
    //     }
    //     return false;
    // }

    public List<Document> getLatestReview (String cId) {
        return gameRepo.getLatestReview(cId);
    }

    public List<Document> getGameById (Integer gameID) {

        return gameRepo.getGameById(gameID);


        
    }

}
