package mvp.model.repository.local;

import android.provider.BaseColumns;

/**
 * Created by Manuel Lorenzo on 23/03/2017.
 */

public final class GameScoreDbContract {
    private GameScoreDbContract() {
    }

    public abstract static class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "Score";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_NAME_PLAYER_NAME = "name";
    }
}
