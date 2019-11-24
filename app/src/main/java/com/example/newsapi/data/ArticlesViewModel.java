package com.example.newsapi.data;

import androidx.lifecycle.ViewModel;

import com.example.newsapi.data.apiResponse.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticlesViewModel extends ViewModel {
    private List<Article> mArticles;

    {
        mArticles = new ArrayList<>();
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }
}
