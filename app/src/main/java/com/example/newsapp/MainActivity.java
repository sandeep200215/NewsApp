package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  RecyclerView recyclerView;
  List<Article> articleList=new ArrayList<>();
  NewsRecyclerAdapter adapter;
  LinearProgressIndicator linearProgressIndicator;
  Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.news_recycler_view);
        linearProgressIndicator=findViewById(R.id.progress_bar);
        searchView=findViewById(R.id.search_view);
        btn1=findViewById(R.id.btn_1);
        btn2=findViewById(R.id.btn_2);
        btn3=findViewById(R.id.btn_3);
        btn4=findViewById(R.id.btn_4);
        btn5=findViewById(R.id.btn_5);
        btn6=findViewById(R.id.btn_6);
        btn7=findViewById(R.id.btn_7);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        recyclerView();
        getNews("GENERAL",null);
    }
    public void changeInprogress(boolean show)
    {
        if(show)
        {
            linearProgressIndicator.setVisibility(View.VISIBLE);

        }
        else {
            linearProgressIndicator.setVisibility(View.INVISIBLE);
        }

    }
    public void recyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }
    public void getNews(String category,String query)
    {
//        changeInprogress(true);
        NewsApiClient newsApiClient=new NewsApiClient("51a67b6ede3849c58eb7a88c1713f13b");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback(){

                    @Override
                    public void onSuccess(ArticleResponse response) {


                       runOnUiThread(()->{
                           changeInprogress(false);
                           articleList=response.getArticles();
                           adapter.updateData(articleList);
                           adapter.notifyDataSetChanged();
                       });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                           Log.i("GOT Failure",throwable.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Button btn=(Button)v;
        String category=btn.getText().toString();
        getNews(category,null);

    }
}