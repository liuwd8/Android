package com.example.liuwd8.httpapi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {
    static final String token = "";
    @GET("/users/{username}/repos")
    Observable<List<Repo>> getRepo(@Path("username") String username);

    @GET("/repos/{username}/{repo_name}/issues")
    Observable<List<IssueData>> getIssue(@Path("username") String username, @Path("repo_name") String repoName);

    @POST("/repos/{username}/{repo_name}/issues")
    Observable<IssueData> postIssue(@Path("username") String username, @Path("repo_name") String repoName, @Body PostData data ,@Query("access_token") String token);
}
