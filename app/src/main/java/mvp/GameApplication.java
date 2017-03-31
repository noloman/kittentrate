package mvp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import mvp.model.di.component.DaggerRepositoryComponent;
import mvp.model.di.component.RepositoryComponent;
import mvp.model.di.module.ApplicationModule;

/**
 * Created by Manuel Lorenzo
 */

public class GameApplication extends Application {
    private RepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        repositoryComponent = DaggerRepositoryComponent.builder()
                .applicationModule(new ApplicationModule((getApplicationContext())))
                .build();

    }

    public RepositoryComponent getRepositoryComponent() {
        return repositoryComponent;
    }
}
