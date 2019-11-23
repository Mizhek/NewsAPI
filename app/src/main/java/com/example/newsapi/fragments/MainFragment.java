package com.example.newsapi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

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

//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MainFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// */
public class MainFragment extends Fragment {

    private ArticlesViewModel mArticlesViewModel;
    private RecyclerView mArticlesRecycle;


//    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArticlesViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
        if (mArticlesViewModel.getArticles().isEmpty()) {
            mArticlesViewModel.setArticles(downloadData());
        }
    }

    private List<Article> downloadData() {

        final NewsApiResponse[] newsApiResponses = {new NewsApiResponse()};
        final List<Article> articles = new ArrayList<>();

        if (articles.isEmpty()) {
            MyApplication.getNewsApi().getArticles("us", "e5c3a6fbf81341fa84a8f45a1c3db179").enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    newsApiResponses[0] = response.body();
                    if (newsApiResponses[0] != null) {
                        articles.addAll(newsApiResponses[0].getArticles());
                    }
                    if (mArticlesRecycle != null) {

                    }
                    Log.d("Response", "onResponse: " + articles.get(0).getTitle());
                    Log.d("Response", "onResponse: " + articles.get(1).getTitle());
                    Log.d("Response", "onResponse: " + articles.get(2).getTitle());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mArticlesRecycle = view.findViewById(R.id.recycler_articles);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);


        return view;
    }
}


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
////        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
