package com.example.newsapp.AdapterHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Models.Article;
import com.example.newsapp.Interfaces.NewsClickListener;
import com.example.newsapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {
        private List<Article> articleList ;
        private NewsClickListener newsClickListener;

        public NewsAdapter(List<Article> articleList, NewsClickListener newsClickListener) {
            this.articleList = articleList;
            this.newsClickListener = newsClickListener;
        }
        @NonNull
        @Override
        public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NewsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_items,parent,false));
        }


        @Override
        public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
            if(articleList != null){
                Article article = articleList.get(position);
                holder.bind(article);
                holder.itemView.setOnClickListener(v->newsClickListener.onNewsClick(articleList.get(position)));

            }else {
                Log.d("tag", "No books");
            }
        }

        @Override
        public int getItemCount() {
            return articleList.size();
        }
        public void filteredList(List<Article> filteredList){
            articleList = filteredList;
            notifyDataSetChanged();
        }
    }

