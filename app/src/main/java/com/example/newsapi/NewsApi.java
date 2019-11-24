package com.example.newsapi;

import com.example.newsapi.data.apiResponse.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    // ?country={country}&apiKey=e5c3a6fbf81341fa84a8f45a1c3db179
    @GET("/v2/top-headlines")
    Call<NewsApiResponse> getArticles(@Query("country") String country,
                                      @Query("pageSize") int pages,
                                      @Query("apiKey") String apiKey);
}
