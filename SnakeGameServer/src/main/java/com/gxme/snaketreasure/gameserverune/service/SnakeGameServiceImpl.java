package com.gxme.snaketreasure.gameserverune.service;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGameUser;
import com.gxme.snaketreasure.gameserverune.repository.SnakeGameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
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
            user = gameUser.get();
            if(score>user.getScore()){
                user.setScore(score);
            }else return;
        }
        snakeGameRepository.save(user);
    }

    private static String getDigitalSignature(String username, int score) {
        try {
            String key = "QxyEWs0GCS7YAb0XklnEteVnVLe1ZrQoQ2L2vyXeR3k=";
            String toHash = "username : " + username + " , score : " + score;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);

            byte[] bytes = mac.doFinal(toHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder ans = new StringBuilder();
            for (byte b : bytes) {
                ans.append(String.format("%02x", b));
            }
            return ans.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public boolean verifySign(String username, String sign, int score) {
        String currSign = getDigitalSignature(username,score);
        return currSign==sign || currSign.equals(sign);
    }
}
