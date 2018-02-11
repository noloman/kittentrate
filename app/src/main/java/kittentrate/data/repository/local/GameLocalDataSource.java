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

import io.reactivex.Observable;
import kittentrate.data.repository.GameDataSource;
import kittentrate.score.PlayerScore;
import kittentrate.utils.Constants;

/**
 * Created by Manuel Lorenzo
 */
public class GameLocalDataSource implements GameDataSource.LocalDataSource {
    private static GameLocalDataSource gameLocalDataSource;
    private SQLiteDatabase sqLiteDatabase;

    private GameLocalDataSource(Context context) {
        GameDbHelper gameDbHelper = new GameDbHelper(context);
        sqLiteDatabase = gameDbHelper.getWritableDatabase();
    }

    public static GameLocalDataSource getInstance(Context context) {
        if (gameLocalDataSource == null) {
            gameLocalDataSource = new GameLocalDataSource(context);
        }
        return gameLocalDataSource;
    }

    static void destroyInstance() {
        gameLocalDataSource = null;
    }

    @Override
    public Observable<List<PlayerScore>> getTopScores() {
        String[] projection = {
                GameScoreDbContract.ScoreEntry._ID,
                GameScoreDbContract.ScoreEntry.COLUMN_NAME_PLAYER_NAME,
                GameScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE
        };

        Cursor c = sqLiteDatabase.query(GameScoreDbContract.ScoreEntry.TABLE_NAME, projection, null, null, null, null, GameScoreDbContract.ScoreEntry.COLUMN_NAME_SCORE + " DESC");

        List<PlayerScore> topScoresList = new ArrayList<>(Constants.NUMBER_TOP_SCORES);
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

        return Observable.just(topScoresList);
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

    @Override
    public void deleteAllScores() {
        sqLiteDatabase.delete(GameScoreDbContract.ScoreEntry.TABLE_NAME, null, null);
    }
}
