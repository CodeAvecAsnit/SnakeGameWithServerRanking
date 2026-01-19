package Entity;

/**
 * @author : Asnit Bakhati
 */
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

    public String getGameUsername() {
        return gameUsername;
    }

    public Integer getScore() {
        return score;
    }
}
