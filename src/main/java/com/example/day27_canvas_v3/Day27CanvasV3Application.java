package com.example.day27_canvas_v3;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.day27_canvas_v3.repository.GameRepository;

import jakarta.json.Json;

@SpringBootApplication
public class Day27CanvasV3Application{

	@Autowired
	GameRepository gameRepo;
	public static void main(String[] args) {
		SpringApplication.run(Day27CanvasV3Application.class, args);
	}

	// @Override
	// public void run(String... args) throws Exception {
	// 	Document doc = gameRepo.findLatest("2be4c24c");

	
	// 		System.out.println(doc);
		


	
	// }
}
