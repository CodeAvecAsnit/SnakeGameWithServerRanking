package com.gxme.snaketreasure.gameserverune.service;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGameUser;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * @author : Asnit Bakhati
 */
public interface SnakeGameService {
    @Transactional
    boolean save(SnakeGameUser snakeGameUser);

    @Transactional
    List<SnakeGameUser> top10() throws Exception;

    @Transactional
    void update(String username, int score) throws Exception;

    boolean verifySign(String username,String sign, int score);
}
