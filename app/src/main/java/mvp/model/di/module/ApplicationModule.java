package mvp.model.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Manuel Lorenzo
 */
@Module
public final class ApplicationModule {
    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    Context providesContext() {
        return context;
    }
}
