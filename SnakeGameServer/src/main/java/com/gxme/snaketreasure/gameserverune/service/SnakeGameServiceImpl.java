package com.gxme.snaketreasure.gameserverune.service;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGameUser;
import com.gxme.snaketreasure.gameserverune.repository.SnakeGameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnakeGameServiceImpl implements SnakeGameService {

    private final SnakeGameRepository snakeGameRepository;

    @Autowired
    public SnakeGameServiceImpl(SnakeGameRepository snakeGameRepository) {
        this.snakeGameRepository = snakeGameRepository;
    }

    @Transactional
    @Override
    public boolean save(SnakeGameUser snakeGameUser) {
        try {
            snakeGameRepository.save(snakeGameUser);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional
    @Override
    public List<SnakeGameUser> top10() throws Exception{
        return snakeGameRepository.findTop10ByOrderByScoreDesc();
    }

    @Transactional
    @Override
    public void update(String username, int score) throws Exception {
        Optional<SnakeGameUser> gameUser = snakeGameRepository.findByGameUsername(username);
        SnakeGameUser user = null ;
        if (gameUser.isEmpty()) {
            user = new SnakeGameUser();
            user.setGameUsername(username);
            user.setScore(score);
        } else {
            SnakeGameUser existingUser = gameUser.get();
            if(existingUser.getScore()>score){
                user.setScore(score);
            }
        }
        snakeGameRepository.save(user);
    }
}
