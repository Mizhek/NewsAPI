package com.example.newsapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapi.data.Article;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {

    private List<Article> mArticles;

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return ((mArticles != null) && (mArticles.size() != 0) ? mArticles.size() : 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumbnail;
        TextView txtTitle;
        TextView txtDate;
        TextView txtSource;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            this.txtTitle = itemView.findViewById(R.id.txt_title);
            this.txtDate = itemView.findViewById(R.id.txt_date);
            this.txtSource = itemView.findViewById(R.id.txt_source);

        }

        public void bind(int position) {

            if (mArticles.size() != 0 && mArticles != null) {
                Article article = mArticles.get(position);
                String imgUrl = article.getUrlToImage();
                String title = article.getTitle();
                String date = article.getPublishedAt();
                String sourceName = article.getSource().getName();

                if (title != null) {
                    txtTitle.setText(title);
                }

                if (date != null) {
                    txtDate.setText(date);
                }

                if (sourceName != null) {
                    txtSource.setText(sourceName);
                }

                if (imgUrl != null) {

                    imgThumbnail.setVisibility(View.VISIBLE);
                    Glide.with(imgThumbnail.getContext())
                            .load(imgUrl)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_error_placeholder)
                            .into(imgThumbnail);
                } else {
                    txtTitle.setMaxLines(3);
                    imgThumbnail.setVisibility(View.GONE);
                }
            }


        }
    }
}
