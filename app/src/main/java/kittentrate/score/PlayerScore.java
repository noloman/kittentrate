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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerScore that = (PlayerScore) o;

        return playerScore == that.playerScore && playerName.equals(that.playerName);
    }

    @Override
    public int hashCode() {
        int result = playerName.hashCode();
        result = 31 * result + playerScore;
        return result;
    }
}
