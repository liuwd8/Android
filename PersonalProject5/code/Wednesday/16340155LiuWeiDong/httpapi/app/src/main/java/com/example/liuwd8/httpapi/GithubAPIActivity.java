package com.example.liuwd8.httpapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubAPIActivity extends AppCompatActivity {
    private EditText searchText;
    private MyRecyclerViewAdapter<Repo> adapter;
    private List<Repo> list;
    private final static String baseUrl = "https://api.github.com";
    private String username = "";
    GitHubService service;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        searchText = findViewById(R.id.searchText);
        list = new ArrayList<Repo>();
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        adapter = new MyRecyclerViewAdapter<Repo>(GithubAPIActivity.this, R.layout.githubrepos_item, list) {
            @Override
            public void convert(MyViewHolder holder, final Repo repo) {
                TextView projectName = holder.getView(R.id.projectName);
                projectName.setText(String.format(getString(R.string.projectName), repo.getName()));
                TextView projectId = holder.getView(R.id.projectId);
                projectId.setText(String.format(getString(R.string.projectId), repo.getId()));
                TextView problem = holder.getView(R.id.problem);
                problem.setText(String.format(getString(R.string.problem), repo.getOpen_issues()));
                TextView description = holder.getView(R.id.description);
                description.setText(String.format(getString(R.string.description), repo.getDescription()));
                CardView cardView = holder.getView(R.id.cardView);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GithubAPIActivity.this, RepoDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("repo", repo.getName());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        OkHttpClient build = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                // 本次实验不需要自定义Gson
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // build 即为okhttp声明的变量，下文会讲
                .client(build)
                .build();
        service = retrofit.create(GitHubService.class);
    }

    public void Search(View view) {
        final String str = searchText.getText().toString();
        if (str.equals("")) {
            return;
        }
        service.getRepo(searchText.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Repo>>() {
                    @Override
                    public void onComplete() {
                        username = str;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        list.clear();
                        if (repos.size() == 0) {
                            Toast.makeText(GithubAPIActivity.this, "没有 Repo", Toast.LENGTH_SHORT).show();
                        } else {
                            for (Repo r: repos) {
                                if (r.getHas_issues()) {
                                    list.add(r);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            Toast.makeText(GithubAPIActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
