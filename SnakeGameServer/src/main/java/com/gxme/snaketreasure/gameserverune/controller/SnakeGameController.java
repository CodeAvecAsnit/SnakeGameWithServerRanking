package com.gxme.snaketreasure.gameserverune.controller;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGameUser;
import com.gxme.snaketreasure.gameserverune.service.SnakeGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Asnit Bakhati
 */
@RestController
@RequestMapping("/api/snakeGame")
public class SnakeGameController{

    private final Logger log = LoggerFactory.getLogger(SnakeGameController.class);
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
    @PutMapping("/update/{username}/{score}")
    public ResponseEntity<String> updateHighScores(
            @PathVariable String username,
            @PathVariable Integer score,
            @RequestParam String sign) {

        log.debug("Request received for: {} with score: {}", username, score);

        if(snakeGameService.verifySign(username, sign, score)) {
            try {
                snakeGameService.update(username, score);
                return ResponseEntity.ok("Success");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        } else return ResponseEntity.status(401).body("Invalid Signature");
    }

    @GetMapping("/connect")
    public ResponseEntity<String> checkConnection(){
        return ResponseEntity.ok("Connected");
    }
}
