package com.gxme.snaketreasure.gameserverune.Services;

import com.gxme.snaketreasure.gameserverune.Entities.SnakeGame;
import com.gxme.snaketreasure.gameserverune.Repositories.SnakeGameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnakeGameService {

    private final SnakeGameRepository snakeGameRepository;

    @Autowired
    public SnakeGameService(SnakeGameRepository snakeGameRepository) {
        this.snakeGameRepository = snakeGameRepository;
    }

    @Transactional
    public boolean save(SnakeGame snakeGame) {
        try {
            snakeGameRepository.save(snakeGame);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional
    public List<SnakeGame> top10() throws Exception{
        return snakeGameRepository.getTopScores();
    }

    @Transactional
    public void update(String username, int score) throws Exception {
        Integer userId = null;

        try {
            userId = snakeGameRepository.getUserId(username);
        } catch (Exception ex) {
            System.out.println("Error fetching user ID: " + ex.getMessage());
            return;
        }
        if (userId == null) {
            SnakeGame newSnakeGame = new SnakeGame();
            newSnakeGame.setGame_username(username);
            newSnakeGame.setScore(score);
            snakeGameRepository.save(newSnakeGame);
        } else {
            Optional<SnakeGame> optional = snakeGameRepository.findById(userId);
            if (optional.isPresent()) {
                SnakeGame existingSnakeGame = optional.get();
                existingSnakeGame.setScore(score);
                snakeGameRepository.save(existingSnakeGame);
            } else {
                System.out.println("Warning: User ID found but record does not exist.");
            }
        }
    }
}
