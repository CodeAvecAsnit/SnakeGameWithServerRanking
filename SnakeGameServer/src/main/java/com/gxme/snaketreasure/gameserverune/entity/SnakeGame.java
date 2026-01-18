package com.gxme.snaketreasure.gameserverune.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="snake_game")
public class SnakeGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column
    private String game_username;

    @Column
    private Integer score;
}
