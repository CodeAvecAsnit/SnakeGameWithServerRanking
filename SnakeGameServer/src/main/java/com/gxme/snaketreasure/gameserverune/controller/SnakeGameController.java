package com.gxme.snaketreasure.gameserverune.controller;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGame;
import com.gxme.snaketreasure.gameserverune.Services.SnakeGameService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snakeGame")
public class SnakeGameController{

    private final SnakeGameService snakeGameService;

    @Autowired
    public SnakeGameController(SnakeGameService snakeGameService) {
        this.snakeGameService = snakeGameService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> SaveGameScore(@RequestBody SnakeGame snakeGame) {
        if(snakeGameService.save(snakeGame)){
            return ResponseEntity.ok("Successfully build");
        }else{
            return ResponseEntity.badRequest().body("Request Timed out");
        }
    }

    @GetMapping("/get/top10")
    public ResponseEntity<List<SnakeGame>> getHighScores(){
        try{
            List<SnakeGame> highScores = snakeGameService.top10();
            return ResponseEntity.ok(highScores);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update/{username},{score}")
    public ResponseEntity<String> updateHighScores(@PathVariable Integer score,@PathVariable String username){
        try{
            snakeGameService.update(username,score);
            return ResponseEntity.ok("Success");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/connect")
    public ResponseEntity<String> checkConnection(){
        return ResponseEntity.ok("Connected");
    }

}
