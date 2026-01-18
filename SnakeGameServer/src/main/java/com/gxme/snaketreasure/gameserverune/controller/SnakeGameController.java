package com.gxme.snaketreasure.gameserverune.controller;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGameUser;
import com.gxme.snaketreasure.gameserverune.service.SnakeGameService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snakeGame")
public class SnakeGameController{

    private final SnakeGameService snakeGameService;

    @Autowired
    public SnakeGameController(@Qualifier("snakeGameServiceImpl")
                                   SnakeGameService snakeGameService) {
        this.snakeGameService = snakeGameService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> SaveGameScore(@RequestBody SnakeGameUser snakeGameUser) {
        if(snakeGameService.save(snakeGameUser)){
            return ResponseEntity.ok("Successfully build");
        }else{
            return ResponseEntity.badRequest().body("Request Timed out");
        }
    }

    @GetMapping("/get/top10")
    public ResponseEntity<List<SnakeGameUser>> getHighScores(){
        try{
            List<SnakeGameUser> highScores = snakeGameService.top10();
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
