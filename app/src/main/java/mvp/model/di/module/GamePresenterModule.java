package mvp.model.di.module;

import dagger.Module;
import dagger.Provides;
import mvp.view.game.GameContract;

/**
 * Created by Manuel Lorenzo
 */

@Module
public class GamePresenterModule {
    private final GameContract.View view;

    public GamePresenterModule(GameContract.View view) {
        this.view = view;
    }

    @Provides
    GameContract.View provideGameContractView() {
        return view;
    }
}