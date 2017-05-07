package kittentrate.data.repository.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kittentrate.data.repository.GameDataSource;
import kittentrate.score.PlayerScore;
import kittentrate.data.rest.NetworkCallback;
import kittentrate.utils.Constants;

/**
 * Created by Manuel Lorenzo
 */
public class GameLocalDataSource implements GameDataSource {
    private SQLiteDatabase sqLiteDatabase;
    private static GameLocalDataSource gameLocalDataSource;

    public static GameLocalDataSource getInstance(Context context) {
        if (gameLocalDataSource == null) {
            gameLocalDataSource = new GameLocalDataSource(context);
        }
        return gameLocalDataSource;
    }

    private GameLocalDataSource(Context context) {
        GameDbHelper gameDbHelper = new GameDbHelper(context);
        sqLiteDatabase = gameDbHelper.getWritableDatabase();
    }

    @Override
    public void getPhotos(NetworkCallback networkCallback) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public List<PlayerScore> getTopScores() {
        String[] projection = {
                GameScoreDbContract.ScoreEntry._ID,
                GameScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME,
                GameScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE
        };

        Cursor c = sqLiteDatabase.query(GameScoreDbContract.ScoreEntry.TABLE_NAME, projection, null, null, null, null, GameScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE + " DESC");

        ArrayList<PlayerScore> topScoresList = new ArrayList<>(Constants.NUMBER_TOP_SCORES);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int score = c.getInt(c.getColumnIndexOrThrow(GameScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE));
                String playerName = c.getString(c.getColumnIndexOrThrow(GameScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME));

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
    public long addTopScore(PlayerScore playerScore) {
        long rowId = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameScoreDbContract.ScoreEntry._ID, UUID.randomUUID().toString());
        contentValues.put(GameScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME, playerScore.getPlayerName());
        contentValues.put(GameScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE, playerScore.getPlayerScore());
        try {
            rowId = sqLiteDatabase.insertOrThrow(GameScoreDbContract.ScoreEntry.TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            Crashlytics.logException(e);
        }
        return rowId;
    }
}
