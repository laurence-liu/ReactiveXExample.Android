package liuuu.laurence.reactivexexample.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitClient {
    private const val GITHUB_BASE_URL = "https://api.github.com/"

    fun gitHubMethods(): GitHubMethods {
        val retrofit = Retrofit.Builder()
                .baseUrl(GITHUB_BASE_URL)
                .client(OkHttpClient().newBuilder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(GitHubMethods::class.java)
    }
}