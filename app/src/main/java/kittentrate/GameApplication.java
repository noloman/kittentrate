package kittentrate;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import kittentrate.data.di.DaggerNetworkComponent;
import kittentrate.data.di.NetworkComponent;
import retrofit2.Retrofit;

/**
 * Created by Manuel Lorenzo
 */

public class GameApplication extends Application {
    private Retrofit retrofit;

    public Retrofit getRetrofit() {
        return retrofit;
    }

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
        NetworkComponent networkComponent = DaggerNetworkComponent.builder()
                .build();
        retrofit = networkComponent.retrofit();
    }
}
