package liuuu.laurence.reactivexexample.api

import io.reactivex.Observable
import io.reactivex.Single
import liuuu.laurence.reactivexexample.model.GitHubResponse.Contributor
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubUserSince
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubUser
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubRepo.GitHubRepo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubMethods {
    @GET("users")
    fun getGitHubUserSingle(
            @Query("since") since: Int
    ): Single<List<GitHubUserSince>>

    @GET("users")
    fun getGitHubUserObservable(
            @Query("since") since: Int
    ): Observable<List<GitHubUserSince>>

    @GET("users/{userName}")
    fun getGitHub(
            @Path("userName") userName: String
    ): Observable<GitHubUser>

    @GET("users/{userName}/repos")
    fun getGitHubRepo(
            @Path("userName") userName: String
    ): Observable<List<GitHubRepo>>

    @GET("repos/{userName}/{repoName}/contributors")
    fun getRepoContributors(
            @Path("userName") userName: String,
            @Path("repoName") repoName: String
    ): Observable<List<Contributor>>
}