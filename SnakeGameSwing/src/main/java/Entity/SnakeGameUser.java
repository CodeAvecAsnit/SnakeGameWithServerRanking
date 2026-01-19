package Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author : Asnit Bakhati
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnakeGameUser {
    Integer userId;
    String gameUsername;
    Integer score;


    public SnakeGameUser(){}
    public SnakeGameUser(Integer userId, String gameUsername, Integer score){
        this.gameUsername= gameUsername;
        this.score=score;
        this.userId= userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGameUsername() {
        return gameUsername;
    }

    public void setGameUsername(String gameUsername) {
        this.gameUsername = gameUsername;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
