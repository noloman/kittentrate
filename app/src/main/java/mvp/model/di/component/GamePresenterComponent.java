package mvp.model.di.component;

import dagger.Component;
import mvp.model.di.module.GamePresenterModule;
import mvp.model.di.scope.FragmentScoped;
import mvp.view.game.GameFragment;

/**
 * Created by Manuel Lorenzo
 */

@FragmentScoped
@Component(dependencies = RepositoryComponent.class, modules = GamePresenterModule.class)
public interface GamePresenterComponent {
    void inject(GameFragment gamePresenter);
}
