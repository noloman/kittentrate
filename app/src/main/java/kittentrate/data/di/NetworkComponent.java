package kittentrate.data.di;

import dagger.Component;
import kittentrate.data.rest.FlickrApiInterceptor;
import kittentrate.data.rest.FlickrService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Manuel Lorenzo on 24/05/2017.
 */
@Kittentrate
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    Retrofit retrofit();

    OkHttpClient okttpClient();

    HttpLoggingInterceptor httpLoggingInterceptor();

    FlickrApiInterceptor interceptor();

    FlickrService flickrService();
}
