package liuuu.laurence.reactivexexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_github.*
import liuuu.laurence.reactivexexample.api.RetrofitClient
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubUserSince
import liuuu.laurence.reactivexexample.utility.SetUpLayoutManager

class GitHubUserActivity : AppCompatActivity() {

    private val mGitHubUserList = ArrayList<GitHubUserSince>()
    private lateinit var mGitHubUserAdapter: GitHubUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, gitHubRecyclerView)

        mGitHubUserAdapter = GitHubUserAdapter(applicationContext, mGitHubUserList)
        gitHubRecyclerView.adapter = mGitHubUserAdapter

        val userObservable = getUserObservable(135)
        val userObserver = getUserObserver()

        userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userObserver)
    }

    private fun getUserObservable(since: Int): Single<List<GitHubUserSince>> {
        return RetrofitClient.gitHubMethods().getGitHubUserSingle(since)
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