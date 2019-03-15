package com.example.liuwd8.httpapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoDetailActivity extends AppCompatActivity {
    private MyRecyclerViewAdapter<IssueData> adapter;
    private List<IssueData> list;
    private static String baseUrl = "https://api.github.com";
    private GitHubService service;
    private String username;
    private String repoName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_repo_detail);
        list = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        adapter = new MyRecyclerViewAdapter<IssueData>(RepoDetailActivity.this, R.layout.githubrepos_item, list) {
            @Override
            public void convert(MyViewHolder holder, IssueData issueData) {
                TextView projectName = holder.getView(R.id.projectName);
                projectName.setText(String.format(getString(R.string.issueName), issueData.getTitle()));
                TextView projectId = holder.getView(R.id.projectId);
                projectId.setText(String.format(getString(R.string.createTime), issueData.getCreated_at()));
                TextView problem = holder.getView(R.id.problem);
                problem.setText(String.format(getString(R.string.problemState), issueData.getState()));
                TextView description = holder.getView(R.id.description);
                description.setText(String.format(getString(R.string.problemDescription), issueData.getBody()));
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(RepoDetailActivity.this));
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            username = bundle.getString("username", "");
            repoName = bundle.getString("repo", "");
            service.getIssue(username, repoName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<List<IssueData>>() {
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<IssueData> issueData) {
                            list.clear();
                            if (issueData.size() == 0) {
                                Toast.makeText(RepoDetailActivity.this, "不存在issue", Toast.LENGTH_SHORT).show();
                            } else {
                                list.addAll(issueData);
                            }
                        }

                        @Override
                        public void onComplete() {
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    public void addIssue(View view) {
        TextView title = findViewById(R.id.title);
        TextView body = findViewById(R.id.body);
        String str1 = title.getText().toString();
        String str2 = body.getText().toString();
        if (username == null || username.equals("") || str1.equals("") || str2.equals("")) {
            return;
        }
        PostData data = new PostData();
        data.setTitle(str1);
        data.setBody(str2);
        service.postIssue(username, repoName, data, GitHubService.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<IssueData>() {
                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNext(IssueData issueData) {
                        list.add(issueData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
