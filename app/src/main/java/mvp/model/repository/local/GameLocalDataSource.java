package mvp.model.repository.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mvp.KittentrateApplication;
import mvp.model.repository.KittentrateDataSource;
import mvp.model.repository.model.PlayerScore;
import mvp.model.rest.NetworkCallback;
import mvp.model.utils.Constants;

/**
 * Created by Manuel Lorenzo
 */

public class GameLocalDataSource implements KittentrateDataSource {
    private SQLiteDatabase sqLiteDatabase;

    public GameLocalDataSource() {
        KittentrateDbHelper cardsDbHelper = new KittentrateDbHelper(KittentrateApplication.getContext(), KittentrateScoreDbContract.ScoreEntry.TABLE_NAME, null, 1);
        sqLiteDatabase = cardsDbHelper.getWritableDatabase();
    }

    @Override
    public void getPhotos(NetworkCallback networkCallback) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public List<PlayerScore> getTopScores() {
        String[] projection = {
                KittentrateScoreDbContract.ScoreEntry._ID,
                KittentrateScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME,
                KittentrateScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE
        };

        Cursor c = sqLiteDatabase.query(KittentrateScoreDbContract.ScoreEntry.TABLE_NAME, projection, null, null, null, null, null);

        ArrayList<PlayerScore> topScoresList = new ArrayList<>(Constants.NUMBER_TOP_SCORES);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int score = c.getInt(c.getColumnIndexOrThrow(KittentrateScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE));
                String playerName = c.getString(c.getColumnIndexOrThrow(KittentrateScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME));

                PlayerScore playerScore = new PlayerScore(playerName, score);
                topScoresList.add(playerScore);
            }
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return topScoresList;
    }

    @Override
    public void addTopScore(PlayerScore playerScore) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KittentrateScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME, playerScore.getPlayerName());
        contentValues.put(KittentrateScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE, playerScore.getPlayerScore());
        sqLiteDatabase.insert(KittentrateScoreDbContract.ScoreEntry.TABLE_NAME, null, contentValues);
    }
}
