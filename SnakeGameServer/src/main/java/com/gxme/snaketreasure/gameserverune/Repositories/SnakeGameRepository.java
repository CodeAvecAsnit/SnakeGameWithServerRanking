package com.gxme.snaketreasure.gameserverune.Repositories;

import com.gxme.snaketreasure.gameserverune.Entities.SnakeGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SnakeGameRepository extends JpaRepository<SnakeGame, Integer> {
    @Query(value = "Select * from snake_game order by score  desc limit 10",nativeQuery = true)
    public List<SnakeGame> getTopScores();

    @Query(value = "SELECT user_id FROM snake_game WHERE game_username = ?1", nativeQuery = true)
    Integer getUserId(String gameUsername);
}
