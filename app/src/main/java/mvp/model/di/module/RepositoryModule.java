package mvp.model.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mvp.model.di.scope.Local;
import mvp.model.di.scope.Remote;
import mvp.model.repository.local.GameLocalDataSource;
import mvp.model.repository.remote.GameRemoteDataSource;

/**
 * Created by Manuel Lorenzo
 */
@Module
public class RepositoryModule {
    @Singleton
    @Provides
    @Local
    GameLocalDataSource provideLocalDataSource(Context context) {
        return new GameLocalDataSource(context);
    }

    @Singleton
    @Provides
    @Remote
    GameRemoteDataSource provideRemoteDataSource() {
        return new GameRemoteDataSource();
    }
}
