package kittentrate.data.rest;

import java.io.IOException;

import kittentrate.utils.Constants;
import manulorenzo.me.kittentrate.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class RetrofitClient {
    private RetrofitClient() {

    }

    public static FlickrService getRetrofitClient() {
        String API_BASE_URL = "https://api.flickr.com/";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("method", "flickr.photos.search")
                        .addQueryParameter("tags", "kitten")
                        // TODO
                        .addQueryParameter("per_page", Constants.CARDS_PER_PAGE)
                        .addQueryParameter("page", "1")
                        .addQueryParameter("api_key", BuildConfig.FLICKR_API_KEY)
                        .addQueryParameter("format", "json")
                        .addQueryParameter("nojsoncallback", "1")
                        .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(logging);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        return retrofit.create(FlickrService.class);
    }
}
