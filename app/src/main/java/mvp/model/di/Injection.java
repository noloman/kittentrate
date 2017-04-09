package mvp.model.di;

import android.content.Context;
import android.support.annotation.NonNull;

import mvp.model.repository.GameRepository;
import mvp.model.repository.local.GameLocalDataSource;
import mvp.model.repository.remote.GameRemoteDataSource;

/**
 * Created by manu on 04/04/2017.
 */

public class Injection {
    public static GameRepository provideRepository(@NonNull Context context) {
        return GameRepository.getInstance(GameLocalDataSource.getInstance(context),
                GameRemoteDataSource.getInstance());
    }

    public static GameLocalDataSource provideLocalDataSource(@NonNull Context context) {
        return GameLocalDataSource.getInstance(context);
    }

    public static GameRemoteDataSource provideRemoteDataSource() {
        return GameRemoteDataSource.getInstance();
    }
}
