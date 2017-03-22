package mvp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Manuel Lorenzo on 22/03/2017.
 */

public class KittentrateApplication extends Application {
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
    }
}
