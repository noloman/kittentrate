package kittentrate.data.repository.local;

import android.provider.BaseColumns;

/**
 * Created by Manuel Lorenzo on 23/03/2017.
 */

final class GameScoreDbContract {
    private GameScoreDbContract() {
    }

    abstract static class ScoreEntry implements BaseColumns {
        static final String TABLE_NAME = "Score";
        static final String COLUMN_NAME_SCORE = "score";
        static final String COLUMN_NAME_PLAYER_NAME = "name";
    }
}
