package com.example.day27_canvas_v3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.day27_canvas_v3.service.GameService;

@Controller
@RequestMapping
public class GameController {

    @Autowired
    GameService gameSvc;

    @GetMapping
    public String getIndex(Model m) {
        return "index";
    }
    
    @PostMapping(path="/review", produces = "application/x-www-form-urlencoded")
    public ModelAndView postComment(@RequestBody MultiValueMap<String,String> form) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("review");
        mav.setStatus(HttpStatusCode.valueOf(200));
        gameSvc.postComment(form);
        return mav;
    }

    @GetMapping(path="/update/{review_id}") 
    public ModelAndView getUpdateComment(@PathVariable String review_id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("edit");
        mav.addObject("review_id", review_id);
        return mav;
    }

    
}
