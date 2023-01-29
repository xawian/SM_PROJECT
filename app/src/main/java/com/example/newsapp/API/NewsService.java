package com.example.newsapp.API;

import com.example.newsapp.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Call<News> findNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );

}
