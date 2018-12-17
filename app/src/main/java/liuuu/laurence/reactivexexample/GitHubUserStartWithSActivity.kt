package liuuu.laurence.reactivexexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_github.*
import liuuu.laurence.reactivexexample.api.RetrofitClient
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubUserSince
import liuuu.laurence.reactivexexample.utility.SetUpLayoutManager

class GitHubUserStartWithSActivity: AppCompatActivity() {

    private lateinit var mGitHubUserAdapter: GitHubUserAdapter
    private val mGitHubUserList = ArrayList<GitHubUserSince>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, gitHubRecyclerView)

        mGitHubUserAdapter = GitHubUserAdapter(applicationContext, mGitHubUserList)
        gitHubRecyclerView.adapter = mGitHubUserAdapter

        val userObservable = getUserObservable()
        val userObserver = getUserObserver()

        userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { githubUser ->
                    githubUser.toObservable()
                }
                .filter {
                    it.login.toLowerCase().startsWith("s")
                }
                .toList()
                .subscribe(userObserver)
    }

    private fun getUserObservable(): Observable<List<GitHubUserSince>> {
        return RetrofitClient.gitHubMethods().getGitHubUserObservable(135)
    }

    private fun getUserObserver(): DisposableSingleObserver<List<GitHubUserSince>> {
        return object : DisposableSingleObserver<List<GitHubUserSince>>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onSuccess(gitHubUserSinceList: List<GitHubUserSince>) {
                mGitHubUserList.clear()
                mGitHubUserList.addAll(gitHubUserSinceList)
                mGitHubUserAdapter.notifyDataSetChanged()
            }
        }
    }
}