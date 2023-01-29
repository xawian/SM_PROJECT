package com.example.newsapp.AdapterHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Models.Article;
import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

public class NewsHolder extends RecyclerView.ViewHolder {
    private ImageView img;
    private TextView title;
    private TextView source;

    public NewsHolder(View itemView) {
        super(itemView);
        Log.d("tag", "SĄ 3");
        img = itemView.findViewById(R.id.article_img);
        title = itemView.findViewById(R.id.article_title);
        source = itemView.findViewById(R.id.article_source);
    }

    public void bind(Article article){
        if(article.getTitle()!=null){
            title.setText(article.getTitle());
            source.setText(article.getSource().getName());
            if(article.getUrlToImage()!=null){
                Picasso.get().load(article.getUrlToImage()).into(img);
            }else{
                img.setImageResource(R.drawable.brakzdjecia);
            }
        }
    }
}
