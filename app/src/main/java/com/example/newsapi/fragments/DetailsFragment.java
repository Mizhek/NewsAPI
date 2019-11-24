package com.example.newsapi.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.newsapi.R;
import com.example.newsapi.data.apiResponse.Article;


public class DetailsFragment extends Fragment {

    static final String ARTICLE_TRANSFER = "transfer";

    private Article mArticle;

    private ImageView mImgPhoto;
    private TextView mTxtTitle;
    private TextView mTxtSource;
    private TextView mTxtDate;
    private TextView mTxtDescription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticle = (Article) getArguments().getSerializable(ARTICLE_TRANSFER);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        setupToolbar(view);
        setupViews(view);
        setViewsData(view);


        return view;
    }

    private void setViewsData(View view) {

        String imageUrl = mArticle.getUrlToImage();
        String title = mArticle.getTitle();
        String source = mArticle.getSource().getName();
        String date = mArticle.getPublishedAt();
        String description = mArticle.getDescription();
        String url = mArticle.getUrl();

        if (imageUrl == null) {
            mImgPhoto.setVisibility(View.GONE);
        } else {
            mImgPhoto.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_error_placeholder)
                    .into(mImgPhoto);
        }

        mTxtTitle.setClickable(true);
        mTxtTitle.setMovementMethod(LinkMovementMethod.getInstance());
        String titleWithUrl = "<a href='" + url + "'> " + title + " </a>";
        mTxtTitle.setText(Html.fromHtml(titleWithUrl));


        if (source == null) {
            mTxtSource.setVisibility(View.GONE);
        } else {
            mTxtSource.setVisibility(View.VISIBLE);
            mTxtSource.setText(source);
        }

        mTxtDate.setText(date);

        if (description == null) {
            mTxtDescription.setVisibility(View.GONE);
        } else {
            mTxtDescription.setVisibility(View.VISIBLE);
            mTxtDescription.setText(description);
        }

    }

    private void setupViews(View view) {
        mImgPhoto = view.findViewById(R.id.img_photo);
        mTxtTitle = view.findViewById(R.id.txt_title);
        mTxtSource = view.findViewById(R.id.txt_source);
        mTxtDate = view.findViewById(R.id.txt_date);
        mTxtDescription = view.findViewById(R.id.txt_description);

    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
    }
}







