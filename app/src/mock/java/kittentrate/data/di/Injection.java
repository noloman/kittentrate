package kittentrate.data.di;

import android.content.Context;
import android.support.annotation.NonNull;

import kittentrate.data.preferences.SharedPreferencesManager;
import kittentrate.data.repository.GameRepository;
import kittentrate.data.repository.local.GameLocalDataSource;
import kittentrate.data.repository.remote.GameRemoteDataSource;

/**
 * Created by manu on 04/04/2017.
 */

public class Injection {
    private Injection() {
    }

    public static GameRepository provideRepository(@NonNull Context context) {
        return GameRepository.Companion.getInstance(GameLocalDataSource.getInstance(context),
                GameRemoteDataSource.Companion.getInstance(),
                new SharedPreferencesManager(context));
    }
}
