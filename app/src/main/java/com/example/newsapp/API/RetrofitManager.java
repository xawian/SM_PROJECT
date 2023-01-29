package com.example.newsapp.API;

import android.util.Log;
import okhttp3.logging.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Retrofit retrofit;
    public static final String NEWS_API_URL = "https://newsapi.org/v2/";

    public static Retrofit getRetrofitManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NEWS_API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d("tag", "SĄ 2");
        }
        return retrofit;
    }




}
