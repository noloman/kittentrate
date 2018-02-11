package kittentrate.data.rest;

import java.io.IOException;

import kittentrate.utils.Constants;
import manulorenzo.me.kittentrate.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Manuel Lorenzo on 17/06/2017.
 */

public class FlickrApiInterceptor implements Interceptor {
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
}
