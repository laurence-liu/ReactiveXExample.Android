package liuuu.laurence.reactivexexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_github_repo.*
import liuuu.laurence.reactivexexample.api.RetrofitClient
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubUser
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubRepo.GitHubRepo
import liuuu.laurence.reactivexexample.model.Repo
import liuuu.laurence.reactivexexample.utility.SetUpLayoutManager

class GitHubRepoActivity : AppCompatActivity() {
    private val mCompositeDisposable = CompositeDisposable()

    private val mUserName = "octocat"

    private val mRepoList = ArrayList<Repo>()
    private lateinit var mRepoAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repo)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, repoRecyclerView)

        mRepoAdapter = RepoAdapter(applicationContext, mRepoList)
        repoRecyclerView.adapter = mRepoAdapter

        val dataObservable = getDataObservable(mUserName)
        val dataObserver = getDataObserver()
        mCompositeDisposable.add(
                dataObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(dataObserver)
        )

        val gitHubRepoObservable = getGitHubRepoObservable(mUserName).replay()
        val gitHubRepoObserver = getGitHubRepoObserver()
        mCompositeDisposable.add(
                gitHubRepoObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(gitHubRepoObserver)
        )

//        val repoConObservable = getRepoConObservable()
        val repoConObserver = getRepoConObserver()
        mCompositeDisposable.add(
                gitHubRepoObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap {
                            mRepoList.toObservable()
                        }
                        .flatMap {
                            getRepoConObservable(it)
                        }
                        .subscribeWith(repoConObserver)
        )

        gitHubRepoObservable.connect()
    }

    private fun getDataObservable(userName: String): Observable<GitHubUser> {
        return RetrofitClient.gitHubMethods().getGitHub(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getDataObserver(): DisposableObserver<GitHubUser> {
        return object : DisposableObserver<GitHubUser>() {
            override fun onNext(gitHubUser: GitHubUser) {
                nameTextView.text = gitHubUser.name
                Picasso.get().load(gitHubUser.avatarUrl).placeholder(R.mipmap.ic_launcher).into(avatarImageView)
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        }
    }

    private fun getGitHubRepoObservable(userName: String): Observable<List<GitHubRepo>> {
        return RetrofitClient.gitHubMethods().getGitHubRepo(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getGitHubRepoObserver(): DisposableObserver<List<GitHubRepo>> {
        return object : DisposableObserver<List<GitHubRepo>>() {
            override fun onNext(gitHubRepoList: List<GitHubRepo>) {
                gitHubRepoList.forEach { mRepoList.add(Repo(repoName = it.name)) }
                mRepoAdapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        }
    }
    private fun getRepoConObservable(repo: Repo): Observable<Repo> {
        return RetrofitClient.gitHubMethods().getRepoContributors(mUserName, repo.repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map{
                    repo.topContributor = it[0].login
                    repo
                }
    }

    private fun getRepoConObserver(): DisposableObserver<Repo> {
        return object : DisposableObserver<Repo>() {
            override fun onNext(repo: Repo) {
                mRepoAdapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}