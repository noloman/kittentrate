package kittentrate.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import kittentrate.utils.Constants;
import manulorenzo.me.kittentrate.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright 2018 Manuel Lorenzo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RetrofitClient {
    private static String API_BASE_URL = "https://api.flickr.com";

    private RetrofitClient() {

    }

    public static ApiService getRetrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.interceptors().add(chain -> {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder()
                    .addQueryParameter("method", "flickr.photos.search")
                    // TODO
                    .addQueryParameter("per_page", Constants.CARDS_PER_PAGE)
                    .addQueryParameter("page", "1")
                    .addQueryParameter("api_key", BuildConfig.FLICKR_API_KEY)
                    .addQueryParameter("format", "json")
                    .addQueryParameter("nojsoncallback", "1")
                    .build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        });

        httpClient.addInterceptor(logging);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        return retrofit.create(ApiService.class);
    }
}
