package kittentrate.data.di;

import android.content.Context;
import android.support.annotation.NonNull;

import kittentrate.data.repository.GameRepository;
import kittentrate.data.repository.local.GameLocalDataSource;

/**
 * Created by manu on 04/04/2017.
 */

public class Injection {
    private Injection() {
    }

    public static GameRepository provideRepository(@NonNull Context context) {
        return GameRepository.getInstance(GameLocalDataSource.getInstance(context),
                GameRemoteDataSource.getInstance());
    }
}
