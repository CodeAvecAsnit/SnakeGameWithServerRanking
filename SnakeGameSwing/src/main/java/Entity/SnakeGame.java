package Entity;

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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getGame_username() {
        return game_username;
    }

    public void setGame_username(String game_username) {
        this.game_username = game_username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
