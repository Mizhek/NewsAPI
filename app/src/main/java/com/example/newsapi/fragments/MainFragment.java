package com.example.newsapi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.newsapi.MainActivity;
import com.example.newsapi.MyApplication;
import com.example.newsapi.R;
import com.example.newsapi.data.ArticlesViewModel;
import com.example.newsapi.data.apiResponse.Article;
import com.example.newsapi.data.apiResponse.NewsApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private static final String APP_PREF = "preferences";
    private static final String COUNTRY_PREF = "country";
    private static final String ARTICLES_NUMBER_PREF = "number";
    private final String mApiKey = "e5c3a6fbf81341fa84a8f45a1c3db179";

    private ArticlesViewModel mArticlesViewModel;
    private RecyclerView mRecycler;
    private ArticlesAdapter mAdapter;
    private String mCountryCode;
    private int mArticlesNumber;

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private View mView;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupPreferences();
        setupArticlesViewModel();
    }

    private void setupArticlesViewModel() {
        mArticlesViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
        if (mArticlesViewModel.isEmpty()) {
            mArticlesViewModel.setArticles(downloadData());
        }
    }

    private void setupPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        mCountryCode = preferences.getString(COUNTRY_PREF, "us");
        mArticlesNumber = preferences.getInt(ARTICLES_NUMBER_PREF, 20);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        switch (mCountryCode) {
            case "us":
                menu.findItem(R.id.country_us).setChecked(true);
                break;
            case "ua":
                menu.findItem(R.id.country_ua).setChecked(true);
                break;
            case "ca":
                menu.findItem(R.id.country_ca).setChecked(true);
                break;
        }

        switch (mArticlesNumber) {
            case 20:
                menu.findItem(R.id.number_20).setChecked(true);
                break;
            case 50:
                menu.findItem(R.id.number_50).setChecked(true);
                break;
            case 100:
                menu.findItem(R.id.number_100).setChecked(true);
                break;
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.country_us:
                changeCountryCode("us");
                break;
            case R.id.country_ua:
                changeCountryCode("ua");
                break;
            case R.id.country_ca:
                changeCountryCode("ca");
                break;
            case R.id.number_20:
                changeArticlesNumber(20);
                break;
            case R.id.number_50:
                changeArticlesNumber(50);
                break;
            case R.id.number_100:
                changeArticlesNumber(100);
                break;
        }

        getActivity().invalidateOptionsMenu();
        return true;

    }

    private void changeCountryCode(String code) {
        mCountryCode = code;
        saveToSharedPreferences(COUNTRY_PREF, code);
        downloadAndApplyNewArticles();
    }

    private void changeArticlesNumber(int number) {
        mArticlesNumber = number;
        saveToSharedPreferences(ARTICLES_NUMBER_PREF, String.valueOf(number));
        downloadAndApplyNewArticles();
    }

    private void downloadAndApplyNewArticles() {
        if (!mArticlesViewModel.isEmpty()) {
            mArticlesViewModel.clearArticles();
            mArticlesViewModel.setArticles(downloadData());
            mAdapter.setArticles(mArticlesViewModel.getArticles());
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<Article> downloadData() {

        final NewsApiResponse[] newsApiResponses = {new NewsApiResponse()};
        final List<Article> articles = new ArrayList<>();

        showLoader();

        if (articles.isEmpty()) {
            MyApplication.getNewsApi()
                    .getArticles(mCountryCode, mArticlesNumber, mApiKey)
                    .enqueue(new Callback<NewsApiResponse>() {
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
                                showRecycler();
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

    private void showRecycler() {
        if (mRecycler != null && mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

    private void showLoader() {

        if (mRecycler != null && mProgressBar != null) {
            mRecycler.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void saveToSharedPreferences(String pref, String value) {
        SharedPreferences preferences = getActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (pref) {
            case COUNTRY_PREF:
                editor.putString(COUNTRY_PREF, value);
                break;
            case ARTICLES_NUMBER_PREF:
                int num = Integer.parseInt(value);
                editor.putInt(ARTICLES_NUMBER_PREF, num);
                break;
        }
        editor.apply();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main, container, false);
        }

        mProgressBar = mView.findViewById(R.id.progressBar);
        mRecycler = mView.findViewById(R.id.recycler_articles);

        setupToolbarWithMenu();

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

    private void setupToolbarWithMenu() {
        mToolbar = mView.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (mArticlesViewModel.getArticles().size() != 0) {
                showRecycler();
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


