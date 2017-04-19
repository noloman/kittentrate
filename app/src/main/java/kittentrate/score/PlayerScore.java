package kittentrate.score;

/**
 * Created by Manuel Lorenzo on 23/03/2017.
 */

public class PlayerScore {
    private String playerName;
    private int playerScore;

    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.playerScore = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
}
