package Entity;

/**
 * @author : Asnit Bakhati
 *
 */

public class SnakeGame {
    Integer user_id;
    String game_username;
    Integer score;

    public SnakeGame(){}
    public SnakeGame(Integer user_id,String game_username,Integer score){
        this.game_username=game_username;
        this.score=score;
        this.user_id=user_id;
    }

    public String getGame_username() {
        return game_username;
    }

    public Integer getScore() {
        return score;
    }
}
