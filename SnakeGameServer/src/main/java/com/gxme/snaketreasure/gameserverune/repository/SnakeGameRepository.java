package com.gxme.snaketreasure.gameserverune.repository;

import com.gxme.snaketreasure.gameserverune.entity.SnakeGameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SnakeGameRepository extends JpaRepository<SnakeGameUser, Integer> {

    List<SnakeGameUser> findTop10ByOrderByScoreDesc();

    Optional<SnakeGameUser> findByGameUsername(String gameUserName);

}
