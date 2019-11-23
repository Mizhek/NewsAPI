package com.example.newsapi;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    public static final String BASE_URL = "https://newsapi.org";
    private static NewsApi newsApi;

    public static NewsApi getNewsApi() {
        return newsApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApi = retrofit.create(NewsApi.class);
    }
}
