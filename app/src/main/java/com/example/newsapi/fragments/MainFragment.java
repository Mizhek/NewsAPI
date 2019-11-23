package com.example.newsapi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapi.ArticlesAdapter;
import com.example.newsapi.MyApplication;
import com.example.newsapi.R;
import com.example.newsapi.data.Article;
import com.example.newsapi.data.ArticlesViewModel;
import com.example.newsapi.data.NewsApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    public static final String TAG = "Main fragment";
    private static final String RECYCLER_VISIBILITY = "recycler_visibility";
    private ArticlesViewModel mArticlesViewModel;
    private RecyclerView mRecycler;
    private ArticlesAdapter mAdapter;

    private ProgressBar mProgressBar;
    private View mView;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        mArticlesViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
        if (mArticlesViewModel.getArticles().isEmpty()) {
            mArticlesViewModel.setArticles(downloadData());
        }
    }

    private List<Article> downloadData() {

        final NewsApiResponse[] newsApiResponses = {new NewsApiResponse()};
        final List<Article> articles = new ArrayList<>();

        if (articles.isEmpty()) {
            MyApplication.getNewsApi().getArticles("us", 100, "e5c3a6fbf81341fa84a8f45a1c3db179").enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    newsApiResponses[0] = response.body();
                    if (newsApiResponses[0] != null) {
                        articles.addAll(newsApiResponses[0].getArticles());
                    }
                    if (mRecycler != null &&
                            mAdapter != null &&
                            mProgressBar != null) {

                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.GONE);
                        mRecycler.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Download error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return articles;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");

        if (mRecycler != null) {
            if (mRecycler.getVisibility() == View.VISIBLE) {
                outState.putBoolean(RECYCLER_VISIBILITY, true);
            } else {
                outState.putBoolean(RECYCLER_VISIBILITY, false);
            }
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");


        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main, container, false);
        }

        mProgressBar = mView.findViewById(R.id.progressBar);
        Toolbar toolbar = mView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        mRecycler = mView.findViewById(R.id.recycler_articles);
        mAdapter = new ArticlesAdapter();
        mAdapter.setArticles(mArticlesViewModel.getArticles());
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setOnItemClickListener(new ArticlesAdapter.ArticleClickListener() {
            @Override
            public void onArticleClick(int position) {
                navigateToDetails(position);
            }

        });
        mRecycler.setAdapter(mAdapter);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        if (savedInstanceState != null) {
            if (mArticlesViewModel.getArticles().size() != 0) {
                mProgressBar.setVisibility(View.GONE);
                mRecycler.setVisibility(View.VISIBLE);
            }
        }

    }

    private void navigateToDetails(int position) {

        Bundle args = new Bundle();
        args.putSerializable(DetailsFragment.ARTICLE_TRANSFER, mAdapter.getArticle(position));

        Navigation.findNavController(mRecycler)
                .navigate(R.id.action_mainFragment_to_detailsFragment, args);
    }


}


