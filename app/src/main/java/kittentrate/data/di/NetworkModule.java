package kittentrate.data.di;

import dagger.Module;
import dagger.Provides;
import kittentrate.data.rest.FlickrApiInterceptor;
import kittentrate.data.rest.FlickrService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static kittentrate.utils.Constants.API_BASE_URL;

/**
 * Created by Manuel Lorenzo on 24/05/2017.
 */
@Module
@Kittentrate
class NetworkModule {
    @Provides
    @Kittentrate
    FlickrService flickrService(Retrofit retrofit) {
        return retrofit.create(FlickrService.class);
    }

    @Provides
    @Kittentrate
    Retrofit retrofit(OkHttpClient client) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        return builder.client(client.newBuilder().build()).build();
    }

    @Provides
    @Kittentrate
    OkHttpClient okHttpClient(FlickrApiInterceptor requestInterceptor, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor);
        return builder.build();
    }

    @Provides
    @Kittentrate
    HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }

    @Provides
    @Kittentrate
    FlickrApiInterceptor flickApiInterceptor() {
        return new FlickrApiInterceptor();
    }
}
