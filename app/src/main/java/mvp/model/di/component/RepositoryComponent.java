package mvp.model.di.component;

import javax.inject.Singleton;

import dagger.Component;
import mvp.model.di.module.ApplicationModule;
import mvp.model.di.module.RepositoryModule;
import mvp.model.repository.GameRepository;

/**
 * Created by Manuel Lorenzo
 */
@Singleton
@Component(modules = {RepositoryModule.class, ApplicationModule.class})
public interface RepositoryComponent {
    GameRepository getGameRepository();
}
